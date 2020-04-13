package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.ini4j.Profile.Section;
import org.ini4j.Wini;

import application.initgui.AmplitudesTab;
import application.initgui.FreqTableProcessor;
import application.initgui.ModbusTab;
import application.initgui.OrdersTab;
import application.initgui.Row;
import exceptions.InvalidData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SampleController {
	private static final Integer ROW_SIZE = 26;
	private static final int DATA_SIZE_BYTES = 2816;
	private static final int DATA_SIZE_REGISTERS = DATA_SIZE_BYTES >> 1;
	private static final byte DATA_SIZE_REGISTERS_ONE_SENDING = 123;
	private static final byte DATA_SENDINGS_QUANTITY = (byte) (Math.floor(DATA_SIZE_REGISTERS / DATA_SIZE_REGISTERS_ONE_SENDING) + 1);
	private static final int ORDERS_DATA_OFFSET = 36;
	private static final int COEFFICIENTS_DATA_OFFSET = 48;
	private static final int COEFFICIENTS_DATA_SIZE = 100;
	private static final int MODBUS_ADDRESS = 1248;
	private static final byte MODBUS_WRITE_FUNCTION = 16;
	private static final byte MODBUS_BROADCAST_ADDRESS = -1;
	private static final int MODBUS_REFRESH_FLASH_MEMORY_ADDRESS_0 = 1406;
	private static final int MODBUS_REFRESH_FLASH_MEMORY_ADDRESS_1 = 1407;
	private Wini settings;
	private ObservableList<Tab> tabs;
	private ModbusTab modbusTab = new ModbusTab();
	private AmplitudesTab amplitudesTab = new AmplitudesTab();
	private OrdersTab ordersTab = new OrdersTab();
	private Stage serialSettingsStage;
	private Short [] data = new Short [DATA_SIZE_BYTES];
	private Alert alertError = new Alert(AlertType.ERROR);
	private Alert goodMessage = new Alert(AlertType.INFORMATION);
	private SerialController serialController;
	
	@FXML
	private MenuItem openIni;
	@FXML
	private MenuItem saveIni;
	@FXML
	private TextField modbusAddress;
	@FXML 
	private TextField amp1, amp2, amp3, amp4, amp5, amp6, amp7, amp8, amp9, amp10, amp11, amp12;
	@FXML 
	private TextField ord1, ord2, ord3, ord4, ord5, ord6, ord7, ord8, ord9, ord10, ord11, ord12;
	@FXML
	private CheckBox broadcastAddress;
	@FXML
	private CheckBox rewriteEEPROM;
	@FXML
	private ProgressBar progress;
	
	final FileChooser fileChooser = new FileChooser();
	
	public void init() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Serial.fxml"));
		loader.load();
		AnchorPane root = (AnchorPane)loader.getRoot();
		Scene scene = new Scene(root, 350, 300);
		serialSettingsStage = new Stage();
		serialSettingsStage.setTitle("Serial settings");
		serialSettingsStage.setScene(scene);
		serialSettingsStage.initModality(Modality.APPLICATION_MODAL);
		serialController = loader.getController();
		serialController.init(this);
	}
	
	private void prepareData() throws InvalidData, InterruptedException {
		Platform.runLater(() -> {
			progress.setProgress(0d);
		});
		initData();
		prepareOrders();
		prepareCoefficients();
		prepareModbus();
		if (rewriteEEPROM.isSelected()) {
			data [MODBUS_REFRESH_FLASH_MEMORY_ADDRESS_0] = 0x1111;
			data [MODBUS_REFRESH_FLASH_MEMORY_ADDRESS_1] = 0x2222;
		}
	}
	
	private List<List<Byte>> data2ModbusSendings() {
		short index = 0;
		List<List<Byte>> sendings = new ArrayList<>();
		for (int i=0; i<DATA_SENDINGS_QUANTITY; i++) {
			List<Byte> sending = new ArrayList<>();
			byte [] address = short2Bytes(index);
			// First register address – high byte
			byte addrHi = address [1];
			// First register address – low byte
			byte addrLo = address [0];
			byte modbusAddress = data[MODBUS_ADDRESS].byteValue();
			if (broadcastAddress.isSelected()) {
				modbusAddress = MODBUS_BROADCAST_ADDRESS;
			}
			sending.add(modbusAddress);
			sending.add(MODBUS_WRITE_FUNCTION);
			sending.add(addrHi);
			sending.add(addrLo);
			sending.add((byte) 0);
			sending.add(DATA_SIZE_REGISTERS_ONE_SENDING);
			sending.add((byte) (DATA_SIZE_REGISTERS_ONE_SENDING << 1));
			for (int j=index, k=0; (k < DATA_SIZE_REGISTERS_ONE_SENDING) && (j < DATA_SIZE_REGISTERS); j++, k++) {
				byte [] value = short2Bytes(data [j]);
				System.out.println(j + " : " + data [j] + " : " + "[" + value [1] + ";" + value [0] + "]");
				sending.add(value [1]);
				sending.add(value [0]);
			}
			// установка количества регистров
			sending.set(5, (byte)((sending.size() - 7) >> 1));
			// установка количества байт
			sending.set(6, (byte)(sending.size() - 7));
			int [] crc = CRC16.getCrc(sending);
			sending.add((byte) crc [0]);
			sending.add((byte) crc [1]);
			
			//sending.stream().forEach(t->System.out.print(String.valueOf(String.format("%02x", t)) + " "));
			
			sendings.add(sending);
			index += DATA_SIZE_REGISTERS_ONE_SENDING;
		}
		return sendings;
	}
	
	private byte [] short2Bytes(short x) {
		byte [] ret = new byte [2];
		ret[0] = (byte)(x & 0xff);
		ret[1] = (byte)((x >> 8) & 0xff);
		return ret;
	}
	
	private void prepareModbus() throws InvalidData {
		try {
			data [MODBUS_ADDRESS] = Short.valueOf(modbusAddress.getText());
		} catch (NumberFormatException e) {
			throw new InvalidData(String.format("Ошибка modbus %s", modbusAddress.getText()));
		}
	}
	
	private void prepareCoefficients() throws InvalidData {
		int index = 0;
		for (Tab tab : tabs) {
			if (tab.getText().startsWith("f")) {
				int localIndex = index;
				String sectionName = tab.getText();
				TableView<Row> table = FreqTableProcessor.getTableView(tab);
				ObservableList<Row> rows = table.getItems();
				boolean exit = false;
				for (Row row : rows) {
					for (int i=0; i<ROW_SIZE; i++) {
						String value = row.getByIndex(i);
						if (value != null && !"".equals(value)) {
							try {
								data [COEFFICIENTS_DATA_OFFSET + localIndex] = Short.valueOf(value);
								//System.out.println(COEFFICIENTS_DATA_OFFSET + "+" + localIndex + "=" + (COEFFICIENTS_DATA_OFFSET + localIndex));
							} catch (NumberFormatException e) {
								throw new InvalidData(String.format("Ошибка при парсинге коэффициентов фильтра: %s", value));
							}
							localIndex++;
						} else {
							exit = true;
							break;
						}
					}
					if (exit) {
						break;
					}
				}
				if (localIndex - index >= COEFFICIENTS_DATA_SIZE) {
					throw new InvalidData(String.format("Порядок фильтра не может превышать %d", COEFFICIENTS_DATA_SIZE));
				}
				index+=COEFFICIENTS_DATA_SIZE;
			}
		}
	}
	
	private void prepareOrders() throws InvalidData {
		int index = 0;
		for (TextField field : Arrays.asList(ord1, ord2, ord3, ord4, ord5, ord6, ord7, ord8, ord9, ord10, ord11, ord12)) {
			try {
				data[ORDERS_DATA_OFFSET + index] = Short.valueOf(field.getText());
			} catch (NumberFormatException e) {
				throw new InvalidData(String.format("Ошибка в порядке фильтра %s", field.getText()));
			}
			index++;
		};
	}
	
	private void initData() {
		for (int i=0; i<DATA_SIZE_BYTES; i++) {
			data [i] = 0;
		}
	}
	
	@FXML
	protected void handleWriteToController(Event event) {			
		try {
			if (!serialController.getSerialPort().isOpen()) {
				serialController.getSerialPort().setBaudRate(serialController.getBaudRate());
				if(!serialController.getSerialPort().openPort()) {
					throw new Exception();
				}
			}
			new Thread() {
				@Override
				public void run() {
					boolean success = false;
					try {
						prepareData();
						List<List<Byte>> sendings = data2ModbusSendings();
						int i = 1;
						int j = 0;
						for (List<Byte> sending : sendings) {
							i = sendOne(sendings, sending, i);
							j++;
							final double step = j;
							Platform.runLater(() -> progress.setProgress(step / sendings.size()));
						}
						success = true;
					} catch (Exception e) {
						Platform.runLater(() -> {
							alertError.setTitle("Ошибка при подготовке данных для записи в контроллер");
							alertError.setContentText(e.getMessage());
							alertError.show();
						});
					}
					finally {
						if (serialController.getSerialPort().isOpen()) {
							serialController.getSerialPort().closePort();
						}
					}
					if (success) {
						Platform.runLater(() -> {
							goodMessage.setTitle("Все пакеты записаны");
							goodMessage.setContentText("Все пакеты записаны");
							goodMessage.show();
						});
					}
				}
			}.start();
		} catch (Exception e) {
			alertError.setTitle("Ошибка при подготовке данных для записи в контроллер");
			alertError.setContentText(e.getMessage());
			alertError.show();
		}
	}
	
	@FXML
	protected void handleWriteToFlash(Event event) {
		
	}
	
	@FXML
	protected void handleOpenSerialSettings(Event event) {
		serialSettingsStage.showAndWait();
	}
	
	@FXML
	protected void handleFileOpen(Event event) {
		File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            openFile(file);
        }
	}
	
	@FXML
	protected void handleFileSave(Event event) {
		File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            saveFile(file);
        }
	}
	
	private void openFile(File file) {
		try{
			settings = new Wini(file);
			for (Tab tab: tabs) {
				Section item = settings.get(tab.getText());
				if (tab.getText().startsWith("f")) {
					populateFilterTable(item, tab);
				}
				else if (ModbusTab.MODBUS_TAB_NAME.equals(tab.getText())) {
					modbusTab.init(tab, settings, modbusAddress);
				}
				else if (AmplitudesTab.AMPLITUDES_TAB_NAME.equals(tab.getText())) {
					amplitudesTab.init(tab, settings, Arrays.asList(amp1, amp2, amp3, amp4, amp5, amp6, amp7, amp8, amp9, amp10, amp11, amp12));
				}
				else if (OrdersTab.ORDERS_TAB_NAME.equals(tab.getText())) {
					ordersTab.init(tab, settings, Arrays.asList(ord1, ord2, ord3, ord4, ord5, ord6, ord7, ord8, ord9, ord10, ord11, ord12));
				}
			}
		}catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
	
	private void saveFile(File file) {
		try{
			settings = new Wini(file);
			saveFTabs();
			saveModbusTab();
			saveAmplitudesTab();
			saveOrdersTab();
			settings.store();
		}catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
	
	private void saveOrdersTab() {
		Arrays.asList(ord1, ord2, ord3, ord4, ord5, ord6, ord7, ord8, ord9, ord10, ord11, ord12).forEach(t -> {
			String id = t.getId();
			settings.put("orders", id, t.getText());
		});
	}
	
	private void saveAmplitudesTab() {
		Arrays.asList(amp1, amp2, amp3, amp4, amp5, amp6, amp7, amp8, amp9, amp10, amp11, amp12).forEach(t -> {
			String id = t.getId();
			settings.put("amplitudes", id, t.getText());
		});
	}
	
	private void saveModbusTab() {
		settings.put("modbus", "address", modbusAddress.getText());
	}
	
	private void saveFTabs() {
		tabs.stream().filter(t -> t.getText().startsWith("f")).forEach(t -> {
			String sectionName = t.getText();
			TableView<Row> table = FreqTableProcessor.getTableView(t);
			ObservableList<Row> rows = table.getItems();
			if (rows != null) {
				settings.remove(sectionName);
				int index = 1;
				for (Row row : rows) {
					index = putRow(sectionName, row, index);
				}
			}
		});
	}
	
	private int putRow(String sectionName, Row row, int index) {
		for (String letter : row.getLetters()) {
			if (!"".equals(row.getByName(letter))) {
				settings.put(sectionName, "k" + index, row.getByName(letter));
				index++;
			}
		}
		return index;
	}
	
	private void populateFilterTable(Section section, Tab tab) {
		List<Row> rows = new ArrayList<>();
		List<String> sRow = null;
		int i=0, j=0;
		for (String key: section.keySet()) {
			if (i % ROW_SIZE == 0) {
				if (i != 0) {
					rows.add(populateRow(sRow));
				}
				sRow = initStringRow();
				j = 0;
			}
			sRow.set(j, section.get(key));
			i++;
			j++;
		}
		if (!sRow.get(0).equals("")) {
			rows.add(populateRow(sRow));
		}
		// плюс 4 строки для возможности расширить = 4*26 = 104
		Row row = populateRow(initStringRow());
		for (int k=0; k<4; k++) {
			rows.add(row);
		}
		addData2Table(tab, rows);
	}
	
	private void addData2Table(Tab tab, List<Row> rows) {
		TableView table = FreqTableProcessor.getTableView(tab);
		ObservableList<Row> rowsDest = FXCollections.observableArrayList();
		for (Row row: rows) {
			rowsDest.add(row);
		}
		table.setItems(rowsDest);
	}
	
	private Row populateRow(List<String> sRow) {
		return new Row(sRow.get(0), sRow.get(1), sRow.get(2), sRow.get(3), sRow.get(4), 
				sRow.get(5), sRow.get(6), sRow.get(7), sRow.get(8), sRow.get(9), sRow.get(10), 
				sRow.get(11), sRow.get(12), sRow.get(13), sRow.get(14), sRow.get(15), 
				sRow.get(16), sRow.get(17), sRow.get(18), sRow.get(19), sRow.get(20), 
				sRow.get(21), sRow.get(22), sRow.get(23), sRow.get(24), sRow.get(25));
	}
	
	private List<String> initStringRow() {
		List<String> sRow = new ArrayList<>();
		for (int i=0; i<ROW_SIZE; i++) {
			sRow.add("");
		}
		return sRow;
	}
	
	public Wini getSettings() {
		return settings;
	}
	
	public void setTabs(ObservableList<Tab> tabs) {
		this.tabs = tabs;
	}
	
	private int sendOne(List<List<Byte>> sendings, List<Byte> sending, int number) 
			throws InterruptedException, InvalidData {
		int counter = 0;
		for (;;) {
			try {
				System.out.println(sending.size());
				sending.forEach(t->System.out.print(String.valueOf(String.format("%02x", (int) t)) + " "));
				System.out.println("--------------------------------------------");
				System.out.println();
				serialController.write(sending);
				Thread.sleep(100);
				int attemptions = 3;
				byte [] ret = serialController.read(attemptions, number == sendings.size() ? 1000 : 100);
				System.out.println("*****************************");
				for (byte b : ret) {
					System.out.print(((int) b) + " ");
				}
				number++;
				System.out.println("*************" + (number-1) + " из " + sendings.size() + "****************");
				break;
			}
			catch (InvalidData e) {
				if (counter < 1) {
					counter++;
					System.out.println("Попытка 2");
				} else {
					throw e;
				}
			}
		}
		return number;
	}
}
