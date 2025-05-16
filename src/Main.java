import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.DatabaseConnection;

public class Main extends Application {
   
    
    @Override
    public void start(Stage primaryStage) throws Exception {
               
        // Chargement de la vue principale
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Gestion de boutique ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
                // Dans votre méthode main() avant launch()
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.verbose", "true");
         // Initialisation de la base de données
         DatabaseConnection.initializeDatabase();
        launch(args);
    }
}
