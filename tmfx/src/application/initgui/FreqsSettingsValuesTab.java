package application.initgui;

import java.util.ArrayList;
import java.util.List;

import org.ini4j.Wini;
import org.ini4j.Profile.Section;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FreqsSettingsValuesTab {

	public void init(Tab tab, Wini settings, TextField freqDiv) {
		Section section = settings.get(tab.getText().toLowerCase());
		if (section != null) {
			FreqValuesRow row = new FreqValuesRow();
			for (String key: section.keySet()) {
				row.setByName(key, section.get(key));
			}
			TableView table = FreqsSettingsTableProcessor.getTableView(tab);
			ObservableList<FreqValuesRow> rowsDest = FXCollections.observableArrayList();
			rowsDest.add(row);
			table.setItems(rowsDest);
		}
		section = settings.get("freqDiv");
		if (section != null) {
			String value = section.get("value");
			freqDiv.setText(value);
		}
	}
	
}
