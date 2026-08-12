package swapnoo_super_shop;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LockScreenController implements Initializable {

    @FXML
    private TextField usenameField;

    @FXML
    private TextField passwordFeild;

    @FXML
    private TextField userField;

    @FXML
    private TextField terminalField;

    String nmf, psf, usrf, trf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void loginOnAction(ActionEvent event) throws IOException {

        nmf = usenameField.getText().trim();
        psf = passwordFeild.getText().trim();
        usrf = userField.getText().trim();
        trf = terminalField.getText().trim();

        if (nmf.isEmpty() || psf.isEmpty() || usrf.isEmpty() || trf.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Error");
            alert.setHeaderText("Information Error");
            alert.setContentText("Please fill in all the information to login.");
            alert.showAndWait();

        } else if (nmf.equals("admin") && psf.equals("admin")) {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("FXMLDocument.fxml")
            );

            Parent root = loader.load();

            FXMLDocumentController controller = loader.getController();

            controller.setUserAndTerminal(usrf, trf);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage =
                    (Stage) usenameField.getScene().getWindow();

            currentStage.close();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Login");
            alert.setContentText("Incorrect username or password.");
            alert.showAndWait();
        }
    }

    @FXML
    private void celarOnAction(ActionEvent event) {

        usenameField.clear();
        passwordFeild.clear();
        userField.clear();
        terminalField.clear();
    }
}