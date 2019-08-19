import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server
{
    private static int uniqueId;
    private ArrayList<ClientThread> ArrayCT;
    private ServerGUI serverInterface;
    private SimpleDateFormat dateFormat;
    private int port;
    private boolean Running;

    public Server(int port)
    {
        this(port, null);
    }

    public Server(int port, ServerGUI serverInterface)
    {
        this.serverInterface = serverInterface;
        this.port = port;
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        ArrayCT = new ArrayList<>();
    }
    @SuppressWarnings({"CatchMayIgnoreException", "ForLoopReplaceableByForEach"})
    public void start()
    {
        Running = true;
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            while(Running)
            {
                display("Server waiting for Clients on port " + port + ".");
                Socket socket = serverSocket.accept();
                if(!Running)
                {
                    break;
                }
                ClientThread t = new ClientThread(socket);
                ArrayCT.add(t);
                t.start();
            }
            try
            {
                serverSocket.close();
                for(int i = 0; i < ArrayCT.size(); ++i)
                {
                    ClientThread tc = ArrayCT.get(i);
                    try
                    {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    }
                    catch(IOException ioE)
                    {
                    }
                }
            }
            catch(Exception e)
            {
                display("Exception closing the server and clients: " + e);
            }
        }
        catch (IOException e)
        {
            String msg = dateFormat.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }

    @SuppressWarnings("CatchMayIgnoreException")
    protected void stop()
    {
        Running = false;
        try
        {
            new Socket("localhost", port);
        }
        catch(Exception e)
        {
        }
    }
    private void display(String msg)
    {
        String time = dateFormat.format(new Date()) + " " + msg;
        if(serverInterface == null)
        {
            System.out.println(time);
        }
        else
        {
            serverInterface.appendEvent(time + "\n");
        }
    }

    private synchronized void broadcast(String message)
    {
        String time = dateFormat.format(new Date());
        String messageLf = time + " " + message + "\n";
        if(serverInterface == null)
        {
            System.out.print(messageLf);
        }
        else
        {
            serverInterface.appendRoom(messageLf);
        }

        for(int i = ArrayCT.size(); --i >= 0;)
        {
            ClientThread ct = ArrayCT.get(i);
            if(!ct.writeMessage(messageLf))
            {
                ArrayCT.remove(i);
                display("Disconnected Client " + ct.username + " removed from list.");
            }
        }
    }

    synchronized void remove(int id)
    {
        for(int i = 0; i < ArrayCT.size(); ++i)
        {
            ClientThread ct = ArrayCT.get(i);
            if(ct.id == id)
            {
                ArrayCT.remove(i);
                return;
            }
        }
    }

    public static void main(String[] args)
    {
        int portNumber = 1500;
        switch(args.length)
        {
            case 1:
                try
                {
                    portNumber = Integer.parseInt(args[0]);
                }
                catch(Exception e)
                {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
                }
                case 0:
                    break;
                default:
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
        }
        Server server = new Server(portNumber);
        server.start();
    }

    class ClientThread extends Thread
    {
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        String username;
        ChatMessage cm;
        String date;
        @SuppressWarnings("CatchMayIgnoreException")
        ClientThread(Socket socket)
        {
            id = ++uniqueId;
            this.socket = socket;
            System.out.println("Thread trying to create Object Input/Output Streams");
            try
            {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput  = new ObjectInputStream(socket.getInputStream());
                username = (String) sInput.readObject();
                display(username + " just connected.");
            }
            catch (IOException e)
            {
                display("Exception creating new Input/output Streams: " + e);
                return;
            }
            catch (ClassNotFoundException e)
            {
            }
            date = new Date().toString() + "\n";
        }
        public void run()
        {
            boolean keepGoing = true;
            while(keepGoing)
            {
                try
                {
                    cm = (ChatMessage) sInput.readObject();
                }
                catch (IOException e)
                {
                    display(username + " Exception reading Streams: " + e);
                    break;
                }
                catch(ClassNotFoundException e2)
                {
                    break;
                }
                String message = cm.getMessage();
                switch(cm.getType())
                {
                    case ChatMessage.MESSAGE:
                        broadcast(username + ": " + message);
                        break;
                    case ChatMessage.LOGOUT:
                        display(username + " disconnected with a LOGOUT message.");
                        keepGoing = false;
                        break;
                    case ChatMessage.WHOISIN:
                        writeMessage("List of the users connected at " + dateFormat.format(new Date()) + "\n");
                        // scan ArrayCT the users connected
                        for(int i = 0; i < ArrayCT.size(); ++i)
                        {
                            ClientThread ct = ArrayCT.get(i);
                            writeMessage((i+1) + ") " + ct.username + " since " + ct.date);
                        }
                        break;
                }
            }
            remove(id);
            close();
        }
        @SuppressWarnings("CatchMayIgnoreException")
        private void close()
        {
            try
            {
                if(sOutput != null) sOutput.close();
            }
            catch(Exception e)
            {

            }
            try
            {
                if(sInput != null) sInput.close();
            }
            catch(Exception e)
            {
            }
            try
            {
                if(socket != null) socket.close();
            }
            catch (Exception e)
            {
            }
        }

        private boolean writeMessage(String msg)
        {
            if(!socket.isConnected())
            {
                close();
                return false;
            }
            try
            {
                sOutput.writeObject(msg);
            }
            catch(IOException e)
            {
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }
    }
}
