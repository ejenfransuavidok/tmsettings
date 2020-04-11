package application.initgui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import exceptions.ColumnOutOfIndex;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

public class FreqTableProcessor implements InitTab {
	private static final int TABLE_SIZE = 100;
	private List<String> columnsName = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
	private Map<String, List<Integer>> columnsData = new HashMap();
	private Map<String, TableColumn> columns = new HashMap();
	private TableView<Row> table;
	
	@Override
	public TableView initTab(Tab tab) {
		table = getTableView(tab);
		addHeadTable();
		return table;
	}
	
	private void addData() {
		//columns.ObservableList rows = FXCollections.observableArrayList();
		ObservableList rows = table.getItems();
		Row r = new Row("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26");
		rows.add(r);
		table.setItems(rows);
	}
	
	public void putData(String col, Integer row, Integer data) throws ColumnOutOfIndex {
		List<Integer> columnData = columnsData.get(col);
		if (columnData == null) {
			columnData = new ArrayList<Integer>();
			columnsData.put(col, columnData);
		}
		if (TABLE_SIZE < row) {
			throw new ColumnOutOfIndex();
		}
		columnData.set(row, data);
		addColumnData(col, columnData);
	}
	
	public void addColumnData(String col, List<Integer> data) {
		TableColumn<Integer, Number> column = columns.get(col);
		List<Integer> d = new ArrayList(data);
		column.setCellValueFactory(cellData -> {
			Integer rowIndex = cellData.getValue();
          	return new ReadOnlyIntegerWrapper(d.get(rowIndex));
		});
		if (columnsData.get(col) == null) {
			columnsData.put(col, new ArrayList<Integer>());
		}
		columnsData.get(col).clear();
		columnsData.get(col).addAll(data);
	}
	
	private void addHeadTable() {
		for (int i=0; i<columnsName.size(); i++) {
			addColumn(columnsName.get(i));
		}
	}
	
	private void addColumn(String columnName) {
		TableColumn<Row, String> column = new TableColumn<Row, String>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<Row, String>(columnName.toLowerCase()));
        table.getColumns().add(column);
        
        column.setOnEditCommit(t -> {
            CellEditEvent<Row, String> evt = (CellEditEvent<Row, String>) t;
            System.out.println(column.getText());
            Row row = evt.getTableView().getItems().get(evt.getTablePosition().getRow());
            row.setByName(column.getText(), evt.getNewValue());
        });
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setEditable(true);
	}
	
	public static TableView getTableView(Tab tab) {
		AnchorPane pane = getAnchorPane(tab);
		Optional<Node> element = pane.getChildren().stream().filter(e -> e.getClass().equals(TableView.class)).findFirst();
		return element.isPresent() ? (TableView) element.get() : null;
	}
	
	private static AnchorPane getAnchorPane(Tab tab) {
		return (AnchorPane) tab.getContent();
	}
}
