import com.hmall.item.ItemApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ItemApplication.class)
@RunWith(SpringRunner.class)
public class MQTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    public void test(){
        String queue = "item.insert.queue";
        String message = "你好";
        rabbitTemplate.convertAndSend(queue,message);
    }
}
