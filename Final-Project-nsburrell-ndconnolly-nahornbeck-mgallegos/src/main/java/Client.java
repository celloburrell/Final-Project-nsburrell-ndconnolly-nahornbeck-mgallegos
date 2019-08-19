import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.io.*;
import java.net.Socket;

public class Client
{
    private ObjectInputStream streamInput;
    private ObjectOutputStream streamOutput;
    private Socket socket;
    private ClientGUI clientInterface;
    private String server, username;
    private int port;
    @FXML
    TextArea main;


    Client(String server, int port, String username, ClientGUI clientInterface) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.clientInterface = clientInterface;
    }



    public boolean startDialogue(TextArea main) {
        try {
            socket = new Socket(server, port);
        }
        catch(Exception ec) {
            display("Error connecting to server:" + ec, main);
            return false;
        }
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg, main);
        try
        {
            streamInput = new ObjectInputStream(socket.getInputStream());
            streamOutput = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO, main);
            return false;
        }
        new ListenFromServer().start();

        try
        {
            streamOutput.writeObject(username);
        }
        catch (IOException eIO) {
            display("Exception doing login : " + eIO, main);
            disconnect();
            return false;
        }
        return true;
    }

    private void display(String msg, TextArea main) {
        main.appendText(msg + "\n");
        System.out.println(msg + "\n");
    }

    void sendMessage(ChatMessage msg) {
        try {
            streamOutput.writeObject(msg);
        }
        catch(IOException e) {
            display("Exception writing to server: " + e, main);
        }
    }


    @SuppressWarnings({"CatchMayIgnoreException"})
    public void disconnect() {
        try {
            if(streamInput != null) streamInput.close();
        }
        catch(Exception e) {}
        try {
            if(streamOutput != null) streamOutput.close();
        }
        catch(Exception e) {}
        try{
            if(socket != null) socket.close();
        }
        catch(Exception e) {}

        if(clientInterface != null)
            clientInterface.connectionFailed();
    }

    public static void main(String[] args) {

    }

    class ListenFromServer extends Thread {
        @SuppressWarnings("CatchMayIgnoreException")
        public void run() {
            while(true) {
                try {
                    String msg = (String) streamInput.readObject();
                    System.out.println(msg);
                    //writeNewMessage(msg);

                   // if (isNullOrEmpty(readMessage()))
                   //     break;
                }
                catch(IOException e) {
                    display("Server has close the connection: " + e, main);
                    break;
                }
                catch(ClassNotFoundException e2) {
                }
            }
        }
    }

}
