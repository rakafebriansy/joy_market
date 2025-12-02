package joy_market.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.models.Admin;
import joy_market.windows.OrderHeaderWindow;
import joy_market.windows.AdminProductWindow;

public class AdminMainWindow {

    private Admin admin;

    public AdminMainWindow(Admin admin) {
        this.admin = admin;
    }

    public void show(Stage stage) {
        stage.setTitle("JoyMarket - Admin Dashboard");
        
        Tab tabOrders = new Tab("Orders");
        tabOrders.setContent(new OrderHeaderWindow().getView());
        tabOrders.setClosable(false);
        
        Tab tabProducts = new Tab("Product");
        tabProducts.setContent(new AdminProductWindow().getView());
        tabProducts.setClosable(false);

        Tab tabProfile = new Tab("Profile");
        tabProfile.setContent(new AdminProfileWindow(admin).getView());
        tabProfile.setClosable(false);

        TabPane tabPane = new TabPane(tabOrders, tabProfile, tabProducts);

        Scene scene = new Scene(tabPane, 900, 600);
        stage.setScene(scene);
        stage.show();
    }
}
