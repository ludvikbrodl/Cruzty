package application;

import javafx.fxml.FXML;

public class TopTabView {
	@FXML private CreatePallets aLoginTabController;

	@FXML private ViewPallets aBookingTabController;
	
	public void initialize() {
		System.out.println("TopTabView initializing");
		
		// send the booking controller ref to the login controller
		// in order to pass data around
		aLoginTabController.setViewTab(aBookingTabController);
	}


}
