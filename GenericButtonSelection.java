
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

public class GenericButtonSelection {

    private Button btnOK = new Button("OK");
    private Button btnCancel = new Button("Cancel");

    public HBox getHBox() {
        btnOK.setPrefWidth(80);
        btnCancel.setPrefWidth(80);
        
        HBox paneButton = new HBox(btnOK, btnCancel);
        
        return paneButton;
    }

    public Button getBtnOK() {
        return btnOK;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }
}