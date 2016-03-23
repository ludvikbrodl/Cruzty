package application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

// controller for both the top tabs and login tab!

public class LoginTab {
    @FXML private Text actiontarget;
    @FXML private ComboBox<String> cookieTypesBox;

    
    private BookingTab bookingTabCtrl;
    private Database db = Database.getInstance();


    @FXML protected void handleSubmitButtonAction(ActionEvent event) {

    	
        if(!db.isConnected()) {
	        // inform the user that there is no check against the database
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Login fail");
	        alert.setHeaderText(null);
	        alert.setContentText("No database connection! Cannot check user credentials.");
	        alert.showAndWait();
        	
        } else {
            String selectedCookieType = cookieTypesBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected: " + selectedCookieType);
            db.createPallet(selectedCookieType);
            bookingTabCtrl.palletCreated();

        }

    }

    public void initialize() {
    	System.out.println("Initializing LoginTab.");
        if(!db.openConnection("db17", "123456")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database error");
            alert.setHeaderText(null);
            alert.setContentText("Could not connect to the database! Check console for details.");
            alert.showAndWait();
        }

        cookieTypesBox.setItems(FXCollections.observableArrayList(db.getCookieTypes()));
    }
        
    // helpers
    // use this pattern to send data down to controllers at initialization
    public void setBookingTab(BookingTab bookingTabCtrl) {
    	System.out.println("LoginTab sets bookingTab:"+bookingTabCtrl);
    	this.bookingTabCtrl = bookingTabCtrl;

    }
}