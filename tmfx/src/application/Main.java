package application;

import application.initgui.FreqTableProcessor;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
			loader.load();
			
			BorderPane root = (BorderPane)loader.getRoot();
			SampleController controller = loader.getController();
			controller.init();
			Scene scene = new Scene(root, 400, 400);
			
			TabPane tabs = (TabPane) scene.lookup("#mainPane");
			controller.setTabs(tabs.getTabs());
			tabs.getTabs().stream().filter(t -> t.getText().startsWith("f")).forEach(new FreqTableProcessor()::initTab);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("TM+");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
