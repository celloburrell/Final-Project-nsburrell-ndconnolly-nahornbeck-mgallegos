import org.junit.Test;
import java.io.*;
import java.net.URL;

public class MessagesTest {

    @Test
    public void ReadMessageTest() {
        try {

            URL url = getClass().getResource("Messages.txt");
            File file = new File(url.getPath());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String messageAfterReading = br.readLine();
            System.out.println(messageAfterReading);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void writeNewMessageTest(){
        try
        {
            URL url = getClass().getResource("Messages.txt");
            File file = new File(url.getPath());
            FileWriter fw = new FileWriter(file,true);
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            fw.write("This is a new written message\n");
            fw.close();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String messageAfterReading = br.readLine();
            System.out.println(messageAfterReading);
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

}