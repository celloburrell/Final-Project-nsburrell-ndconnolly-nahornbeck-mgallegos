import org.junit.Assert;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UILaunchTests {

    @Test
    public void TestChatMessage(){
        ChatMessage message = new ChatMessage(1, "Hello");
        Assert.assertEquals("Hello", message.getMessage());
    }

    @Test
    public void TestTypeChatMessage(){
        ChatMessage type = new ChatMessage(1, "Hello");
        Assert.assertEquals(1, type.getType());

    }

    @Test
    public void DisplayDateAndTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(new Date());
        System.out.println(time);
    }

}