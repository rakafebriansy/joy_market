package joy_market.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.models.Admin;
import joy_market.windows.OrderHeaderWindow;
import joy_market.windows.AdminProductWindow;

// This class represents the main dashboard window for the Admin user
public class AdminMainWindow {
    
    // Stores the currently logged-in admin's data
    private Admin admin;
    
    // Constructor that receives the admin object
    public AdminMainWindow(Admin admin) {
        this.admin = admin;
    }

    // Method to display the Admin Dashboard window
    public void show(Stage stage) {
        // Set the window title
        stage.setTitle("JoyMarket - Admin Dashboard");
        
        // Create a tab for viewing and managing orders
        Tab tabOrders = new Tab("Orders");
        tabOrders.setContent(new OrderHeaderWindow().getView()); // Load Order view
        tabOrders.setClosable(false); // Prevent closing the tab

       // Create a tab for managing products 
        Tab tabProducts = new Tab("Product");
        tabProducts.setContent(new AdminProductWindow().getView()); // Load Product management view
        tabProducts.setClosable(false);
        
        // Create a tab for viewing and editing admin profile
        Tab tabProfile = new Tab("Profile");
        tabProfile.setContent(new AdminProfileWindow(admin).getView()); // Load Profile view
        tabProfile.setClosable(false);
        
        // Add all tabs into a tab pane
        TabPane tabPane = new TabPane(tabOrders, tabProfile, tabProducts);
        
        // Create and set the scene (window content) with the tab pane
        Scene scene = new Scene(tabPane, 900, 600);
        stage.setScene(scene);
        stage.show(); // Display the window
    }
}
