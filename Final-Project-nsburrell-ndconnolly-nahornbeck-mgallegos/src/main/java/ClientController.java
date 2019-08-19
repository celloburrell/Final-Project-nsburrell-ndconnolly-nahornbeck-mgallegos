import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientController {


    private Client client;
    SimpleDateFormat dateFormat;

    @FXML
    TextArea main;
    @FXML
    TextField usernameInput;
    @FXML
    TextField message;
    @FXML
    Button send;
    @FXML
    Button loginOrOutBtn;
    @FXML
    TextField serverAddressNum;
    @FXML
    TextField portNumber;

    public void loginOrOut() {
        if (loginOrOutBtn.getText().equals("Login")) {

            String username = usernameInput.getText().trim();
            if (username.length() == 0)
                return;
            String serverAddress = serverAddressNum.getText().trim();
            if (serverAddress.length() == 0)
                return;
            String portAddress = portNumber.getText().trim();
            if (portAddress.length() == 0)
                return;
            int port;
            try {
                port = Integer.parseInt(portAddress);
            } catch (Exception en) {
                return;
            }

            client = new Client(serverAddress, port, username, null);

            if (!client.startDialogue(main))
                return;

            loginOrOutBtn.setText("Logout");
            serverAddressNum.setDisable(true);
            usernameInput.setDisable(true);
            portNumber.setDisable(true);

        } else {

            client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, message.getText()));
            main.clear();
            loginOrOutBtn.setText("Login");
            serverAddressNum.setDisable(false);
            usernameInput.setDisable(false);
            portNumber.setDisable(false);
        }

    }

    public void sendMessage() {
        if (!message.getText().trim().equals("")) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            String time = dateFormat.format(new Date());
            client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, message.getText()));
            main.appendText(usernameInput.getText() + ": " + time + " " + message.getText() + "\n");
            message.clear();
        }
    }
}