
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

public class GenericButtonSelection {

    private Button btnOK;
    private Button btnCancel;

    public HBox getGenericButtonHBox() {
        btnOK = new Button("OK");
        btnOK.setPrefWidth(80);

        btnCancel = new Button("Cancel");
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