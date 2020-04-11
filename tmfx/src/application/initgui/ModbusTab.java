package application.initgui;

import org.ini4j.Wini;
import org.ini4j.Profile.Section;

import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

public class ModbusTab {
	
	public static final String MODBUS_TAB_NAME = "Modbus";
	
	public void init(Tab tab, Wini settings, TextField modbusAddress) {
		Section item = settings.get(tab.getText().toLowerCase());
		String value = item.get("address");
		modbusAddress.setText(value);
	}
	
}
