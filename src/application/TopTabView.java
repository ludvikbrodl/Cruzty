package application;

import javafx.scene.Parent;
import javafx.fxml.FXML;

public class TopTabView {
	@FXML private Parent aLoginTab;
	@FXML private LoginTab aLoginTabController;

	@FXML private Parent aBookingTab;
	@FXML private BookingTab aBookingTabController;
	
	public void initialize() {
		System.out.println("TopTabView initializing");
		
		// send the booking controller ref to the login controller
		// in order to pass data around
		aLoginTabController.setBookingTab(aBookingTabController);
	}

    public BookingTab getBookingTab() {
        return aBookingTabController;
    }
}
