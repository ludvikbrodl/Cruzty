package application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;


public class ViewPallets {
    @FXML
    private ListView<String> palletList;

    @FXML
    private Label palletId;
    @FXML
    private Label orderId;
    @FXML
    private Label isBlocked;
    @FXML
    private Label productionDate;
    @FXML
    private Label cookieTypeLabel;
    @FXML
    private Label locatiolabel;
    @FXML
    private Label deliveryDateLabel;
    @FXML
    private Label customerLabel;


    @FXML
    private Button blockButton;

    @FXML
    private TextField filterFromDate;
    @FXML
    private TextField filterToDate;
    @FXML
    private CheckBox filterBlocked;
    @FXML
    private ComboBox<String> cookieTypes;


    private Database db = Database.getInstance();

    public void initialize() {

        // set up listeners for the movie list selection
        palletList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> {
                    // need to update the date list according to the selected movie
                    // update also the details on the right panel
                    fillPalletInfo(newV);
                });


        blockButton.setOnAction(
                (event) -> {
                    String palletId = palletList.getSelectionModel().getSelectedItem();
                    db.blockPallet(palletId);
                    fillPalletInfo(palletId);
                });
        fillPalletList(false);
        cookieTypes.setItems(FXCollections.observableArrayList(db.getCookieTypes()));

    }


    @FXML
    protected void applyFilterAction(ActionEvent event) {
        fillPalletList(true);
    }

    private void fillPalletList(boolean applyFilter) {
        List<String> palletsIds;

        if (!applyFilter) {
            palletsIds = db.getAllPalletIds();
        } else {
            String from = filterFromDate.getText();
            String to = filterToDate.getText();
            String cookieName;
            if (from.equals("")) {
                from = null;
            }
            if (to.equals("")) {
                to = null;
            }
            if(!cookieTypes.getSelectionModel().isEmpty()){
                cookieName = cookieTypes.getSelectionModel().getSelectedItem();
            } else {
                cookieName = null;
            }
            palletsIds = db.getFilteredPalletIds(from, to, filterBlocked.isSelected(), cookieName);
        }
        palletList.setItems(FXCollections.observableList(palletsIds));
        // remove any selection
        palletList.getSelectionModel().clearSelection();
        fillPalletInfo(null);
    }


    private void fillPalletInfo(String idOfPallet) {
        if (idOfPallet == null) {
            palletId.setText("");
            orderId.setText("");
            isBlocked.setText("");
            productionDate.setText("");
            cookieTypeLabel.setText("");
            customerLabel.setText("");
            deliveryDateLabel.setText("");
            locatiolabel.setText("");
            return;
        }
        Pallet p = db.getPallet(idOfPallet);
        palletId.setText(p.id);
        orderId.setText(p.orderId);
        isBlocked.setText(p.isBlocked + "");
        productionDate.setText(p.prodDate);
        cookieTypeLabel.setText(p.cookieName);
        customerLabel.setText(p.customer);
        deliveryDateLabel.setText(p.deliveryDate);
        locatiolabel.setText(p.location);

    }

    public void palletCreated() {
        fillPalletList(false);
    }

}
