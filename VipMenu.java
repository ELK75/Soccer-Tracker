
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.AlertType;

public class VipMenu {

    private String correctPassword;
    private Stage stage;
    private boolean isVip = false;

    public VipMenu(String correctPassword) {
        this.correctPassword = correctPassword;
    }
    
    public void start() {

        GenericTextBox vipTextBox = new GenericTextBox("VIP Access", "Password:");
        this.stage = vipTextBox.getStage();

        // TODO Change so user can just use enter
        //vipTextBox.getTextField().setOnAction(e -> btnOKClick(vipTextBox.getTextField().getText()));
        vipTextBox.getBtnOK().setOnAction(e -> btnOKClick(vipTextBox.getTextField().getText()));
        vipTextBox.getBtnCancel().setOnAction(e -> stage.close());

        stage.showAndWait();
    }

    public void btnOKClick(String attemptedPassword) {
        Alert prompt = new Alert(AlertType.INFORMATION);
        prompt.setHeaderText("Access Details");

        if (attemptedPassword.equals(correctPassword)) {
            isVip = true;
            prompt.setContentText("Access Granted");
        } else {
            prompt.setContentText("Access Denied");
        }
        prompt.showAndWait();
        stage.close();
    }

    public void btnCancelClick(Stage stage) {
        stage.close();
    }

    public boolean getIsVip() {
        return isVip;
    }
}