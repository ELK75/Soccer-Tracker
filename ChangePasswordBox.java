//
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.text.*;
import java.util.*;

public class ChangePasswordBox {

    private MainUser mainUser;
    private int menuWidth = 400;
    private int menuHeight = 350;

    public ChangePasswordBox(MainUser mainUser) {
        this.mainUser = mainUser;
    }

    public void changePassword() {

        Stage changePasswordStage = new Stage();

        TextField[] inputtedPasswords = {
            new TextField(), new TextField(), new TextField()
        };
        Label[] passwordLabels = {
            new Label("Current Password:"), new Label("New Password:"),
            new Label("Reenter New Password:")
        };

        VBox mainPane = new VBox();

        // displays each label and a text box next to each other
        for (int i = 0; i < 3; i++) {
            passwordLabels[i].setPrefWidth(180);
            inputtedPasswords[i].setPrefWidth(200);
            HBox labelsAndTextRow = new HBox(passwordLabels[i], inputtedPasswords[i]);
            labelsAndTextRow.setPadding(new Insets(20, 10, 5, 10));
            mainPane.getChildren().add(labelsAndTextRow);
        }

        Button btnOK = new Button("OK");
        btnOK.setPrefWidth(80);
        btnOK.setOnAction(e -> changeToNewPassword(inputtedPasswords, changePasswordStage));

        Button btnCancel = new Button("Close");
        btnCancel.setPrefWidth(80);
        btnCancel.setOnAction(e -> changePasswordStage.close());

        HBox buttonPane = new HBox(btnOK, btnCancel);
        buttonPane.setPadding(new Insets(30, 10, 10, 230));

        mainPane.getChildren().add(buttonPane);

        Scene scene = new Scene(mainPane, menuWidth, menuHeight-100);
        changePasswordStage.setTitle("Change Password");
        changePasswordStage.setScene(scene);
        changePasswordStage.showAndWait();
    }

    public void changeToNewPassword(TextField[] inputtedPassword, Stage changePasswordStage) {
        if (inputtedPassword[0].getText().equals(mainUser.getPassword())) {

            if (inputtedPassword[1].getText().equals(inputtedPassword[2].getText())) {
                mainUser.setPassword(inputtedPassword[1].getText());
                Dialog.showMessage("Password", "Password Updated", null);
                changePasswordStage.close();
            } else {
                Dialog.showMessage("Password", "Password Mismatch", null);
            }

        } else {
            Dialog.showMessage("Password", "Wrong Password", null);
        }
    }
}