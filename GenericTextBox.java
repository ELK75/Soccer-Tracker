
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.AlertType;

public class GenericTextBox {

    private Stage stage;
    private String title;
    private String text;
    private TextField txtInput;
    private Button btnOK;
    private Button btnCancel;

    public GenericTextBox(String title, String text) {
        this.title = title;
        this.text = text;
    }
    
    public Stage getStage() {

        this.stage = new Stage();

        // Enter Password Label
        Label lblText = new Label(text);
        lblText.setFont(new Font(15));
        lblText.setPrefWidth(125);

        // Text field where user enters password
        TextField txtInput = new TextField();
        txtInput.setPrefColumnCount(20);
        txtInput.setMaxWidth(Double.MAX_VALUE);
        this.txtInput = txtInput;
        
        // Setting password pane
        HBox paneText = new HBox(lblText, txtInput);
        paneText.setPadding(new Insets(35, 20, 10, 10));

        // Ok Button
        this.btnOK = new Button("OK");
        btnOK.setPrefWidth(80);

        // Cancel Button
        this.btnCancel = new Button("Close");
        btnCancel.setPrefWidth(80);

        // Setting button pane
        HBox paneButton = new HBox(btnOK, btnCancel);
        paneButton.setPadding(new Insets(10, 20, 10, 215));

        // Setting a main pane
        BorderPane paneMain = new BorderPane();
        paneMain.setTop(paneText);
        paneMain.setCenter(paneButton);


        // Setting Scene
        Scene scene = new Scene(paneMain, 400, 125);
        stage.setScene(scene);
        stage.setTitle(title);
        return stage;
    }

    public TextField getTextField() {
        return txtInput;
    }

    public Button getBtnOK() {
        return btnOK;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }
}