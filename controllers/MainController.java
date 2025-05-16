package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

public class MainController {

     @FXML
    private Button deconnexionButton;

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
    private void logout(ActionEvent event) {
        // Demander confirmation avant déconnexion
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de déconnexion");
        confirmAlert.setHeaderText("Êtes-vous sûr de vouloir vous déconnecter ?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Fermer la fenêtre actuelle
                Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                currentStage.close();
                
                // Ouvrir la fenêtre de login
                openLoginWindow();
                
                // Journalisation
                System.out.println("Utilisateur déconnecté avec succès");
                
            } catch (Exception e) {
                showError("Erreur de déconnexion", "Une erreur est survenue lors de la déconnexion");
                e.printStackTrace();
            }
        }
    }

    private void openLoginWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
        Scene scene = new Scene(loader.load(),500,500);
        Stage stage = new Stage();
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
