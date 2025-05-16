package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class MainController {
    @FXML
    public void handleProducts(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
           
            Scene scene = new Scene(loader.load(),850,700);
            Stage stage = new Stage();
            stage.setTitle("Gestion des Produits");
            stage.setScene(scene);
            stage.show();

            // Fermer la fenêtre actuelle (menu principal)
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCategories(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CategoryView.fxml"));
           
            Scene scene = new Scene(loader.load(),850,700);
            Stage stage = new Stage();
            stage.setTitle("Gestion des Categorys");
            stage.setScene(scene);
            stage.show();

            // Fermer la fenêtre actuelle (menu principal)
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        // Implémentez la logique de déconnexion
        System.out.println("Déconnexion...");
    }

}
