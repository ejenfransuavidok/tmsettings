package application.initgui;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

public class FreqsSettingsTableProcessor implements InitTab {

	public static final String TAB_FREQS_SETTINGS_VALUES_NAME = "Frequencies";
	private TableView<FreqValuesRow> table;
	private List<String> columnsName = Arrays.asList("f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "f10", "f11", "f12");
	
	@Override
	public TableView initTab(Tab tab) {
		table = getTableView(tab);
		addHeadTable();
		return table;
	}
	
	private void addHeadTable() {
		for (int i=0; i<columnsName.size(); i++) {
			addColumn(columnsName.get(i));
		}
	}
	
	private void addColumn(String columnName) {
		TableColumn<FreqValuesRow, String> column = new TableColumn<FreqValuesRow, String>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<FreqValuesRow, String>(columnName.toLowerCase()));
        table.getColumns().add(column);
        
        column.setOnEditCommit(t -> {
            CellEditEvent<FreqValuesRow, String> evt = (CellEditEvent<FreqValuesRow, String>) t;
            System.out.println(column.getText());
            FreqValuesRow row = evt.getTableView().getItems().get(evt.getTablePosition().getRow());
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
