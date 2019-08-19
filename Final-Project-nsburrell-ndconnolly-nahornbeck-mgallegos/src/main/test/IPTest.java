import org.junit.Assert;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressWarnings("unused")
public class IPTest {

    private ObjectInputStream sInput;
    public ObjectOutputStream sOutput;
    public ClientGUI cg;
    private Socket socket;

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @org.junit.Test
    public void testGetIP()
    {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            Assert.assertTrue(ip.getHostAddress().trim().equals("10.2.238.230"));
        }
        catch(UnknownHostException e) {
            Assert.assertTrue(false);
        }
    }

    @SuppressWarnings("CatchMayIgnoreException")
    @org.junit.Test
    public void disconnectTest() {
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception e) {}
        try {
            if(sOutput != null) sOutput.close();
        }
        catch(Exception e) {}
        try{
            if(socket != null) socket.close();
        }
        catch(Exception e) {}
        if(cg != null)
            cg.connectionFailed();
    }
}
