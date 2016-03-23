package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;

import java.util.List;


public class BookingTab {


    // table references
    @FXML
    private ListView<String> palletList;

    // show info references
    @FXML
    private Label palletId;
    @FXML
    private Label orderId;
    @FXML
    private Label isBlocked;
    @FXML
    private Label productionDate;

    @FXML
    private Button blockButton;


    private Database db = Database.getInstance();

    public void initialize() {
        System.out.println("Initializing BookingTab");


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
        fillPalletList();
    }


    private void fillPalletList() {
        List<String> palletsIds = db.getAllPalletIds();

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
            return;
        }
        Pallet p = db.getPallet(idOfPallet);
        palletId.setText(p.id);
        orderId.setText(p.orderId);
        isBlocked.setText(p.isBlocked + "");
        productionDate.setText(p.prodDate);
    }

    public void palletCreated() {
        fillPalletList();
    }

}
