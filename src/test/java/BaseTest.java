import com.lj.main.WebAutoRemoteMain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebAutoRemoteMain.class)
public class BaseTest {
    @Before
    public void init() {
        System.out.println("Before 单元测试开始");
    }

    @Test
    public void test(){
    	System.out.println("hello world!");
    }
    
    @After
    public void after() {
        System.out.println("After 单元测试结束");
    }
    
}