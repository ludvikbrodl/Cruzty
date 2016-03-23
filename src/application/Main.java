package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			// BorderPane root = new BorderPane();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TopTab.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root,600,440);
			scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
			//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// obtain main controller
			TopTabView wc = (TopTabView) loader.getController();


			// opening database connection
			/* --- TODO: change xxx to your user name, yyy to your passowrd --- */	        

			// show the main window
			primaryStage.setTitle("Cruzty Kookies");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void stop() {
		// close the database here

		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
