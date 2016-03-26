package application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class LoginTab {
    @FXML
    private ComboBox<String> cookieTypesBox;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private ComboBox<String> cookieTypesBoxCountPallets;
    @FXML
    private Label palletCount;

    @FXML
    private ComboBox<String> orderCustomerComboBox;



    private BookingTab bookingTabCtrl;
    private Database db = Database.getInstance();


    @FXML
    protected void handleCountPalletsOfTypeAction(ActionEvent event) {
        if (!db.isConnected()) {
            // inform the user that there is no check against the database
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Not Connected");
            alert.setHeaderText(null);
            alert.setContentText("No database connection! Cannot check user credentials.");
            alert.showAndWait();

        } else {
            String selectedCookieType = cookieTypesBoxCountPallets.getSelectionModel().getSelectedItem();
            palletCount.setText("Count: " + db.getNumPalletsProduced(startDateField.getText(), endDateField.getText(), selectedCookieType));

        }
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        if (!db.isConnected()) {
            // inform the user that there is no check against the database
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login fail");
            alert.setHeaderText(null);
            alert.setContentText("No database connection! Cannot check user credentials.");
            alert.showAndWait();

        } else {
            String selectedCookieType = cookieTypesBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected: " + selectedCookieType);
            String orderId = null;
            if(!orderCustomerComboBox.getSelectionModel().isEmpty()){ //format is "orderId-customerName"
                orderId = orderCustomerComboBox.getSelectionModel().getSelectedItem().split("-")[0];
                orderCustomerComboBox.getSelectionModel().clearSelection();
            }
            db.createPallet(selectedCookieType, orderId);
            bookingTabCtrl.palletCreated();
        }

    }

    public void initialize() {
        System.out.println("Initializing LoginTab.");
        if (!db.openConnection("db17", "123456")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database error");
            alert.setHeaderText(null);
            alert.setContentText("Could not connect to the database! Check console for details.");
            alert.showAndWait();
        }

        cookieTypesBox.setItems(FXCollections.observableArrayList(db.getCookieTypes()));
        cookieTypesBoxCountPallets.setItems(FXCollections.observableArrayList(db.getCookieTypes()));
        orderCustomerComboBox.setItems(FXCollections.observableArrayList(db.getAllOrders()));

    }

    // helpers
    // use this pattern to send data down to controllers at initialization
    public void setBookingTab(BookingTab bookingTabCtrl) {
        System.out.println("LoginTab sets bookingTab:" + bookingTabCtrl);
        this.bookingTabCtrl = bookingTabCtrl;

    }
}