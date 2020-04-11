package application.initgui;

import org.ini4j.Wini;

import java.util.List;

import org.ini4j.Profile.Section;

import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

public class AmplitudesTab {
public static final String AMPLITUDES_TAB_NAME = "Amplitudes";
	
	public void init(Tab tab, Wini settings, List<TextField> amplitudes) {
		Section item = settings.get(tab.getText().toLowerCase());
		amplitudes.forEach(t -> {
			String id = t.getId(); 
			String value = item.get(id); 
			t.setText(value);
		});
	}
}
