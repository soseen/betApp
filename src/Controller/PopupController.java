package Controller;

import Model.HibernateUtil;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class PopupController {



    @FXML private VBox popupWindow;


    HibernateUtil hu = new HibernateUtil();

    public void  initApp() throws IOException {
        Stage popup = (Stage) popupWindow.getScene().getWindow();

        try {
            hu.start();
            hu.join();

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/AppView.fxml"));
            Stage appStage = new Stage();
            appStage.setScene(new Scene(root));
            popup.close();
            appStage.show();


        } catch (RuntimeException e) {
            throw e;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}


