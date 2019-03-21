import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lj.autoRemote.beans.ServerInfoBean;
import com.lj.main.WebAutoRemoteMain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
    	String jsonStr = "[{\"serverPath\":\"/home/yanfa_ro/\",\"bakDir\":\"/home/yanfa_ro/web-autoremote-bak/\",\"upDir\":\"/home/yanfa_ro/web-autoremote-up/\",\"ip\":\"192.168.190.58\",\"serverName\":\"autoRemoteWeb\",\"runCommand\":\"bash start.sh test\",\"id\":1,\"state\":\"\",\"psCommand\":\"ps -ef |grep web-autoremote\"}, {\"serverPath\":\"/home/yanfa_ro/\",\"bakDir\":\"/home/yanfa_ro/web-autoremote-bak/\",\"upDir\":\"/home/yanfa_ro/web-autoremote-up/\",\"ip\":\"172.17.33.109\",\"serverName\":\"autoRemoteWeb\",\"runCommand\":\"bash start.sh test\",\"id\":2,\"state\":\"\",\"psCommand\":\"ps -ef |grep web-autoremote\"}, {\"serverPath\":\"/home/yanfa_ro/\",\"bakDir\":\"/home/yanfa_ro/web-autoremote-bak/\",\"upDir\":\"/home/yanfa_ro/web-autoremote-up/\",\"ip\":\"172.17.33.111\",\"serverName\":\"autoRemoteWeb\",\"runCommand\":\"bash start.sh test\",\"id\":3,\"state\":\"\",\"psCommand\":\"ps -ef |grep web-autoremote\"}]";
//        List<JSONObject> serverInfoBeanList = JSON.parseObject(jsonStr,List.class);
        List<ServerInfoBean> serverInfoBeanList = JSON.parseArray(jsonStr,ServerInfoBean.class);
        System.out.println(serverInfoBeanList.toString());
        for (ServerInfoBean serverInfoBean : serverInfoBeanList) {
//            System.out.println(jSONObject.get("ip"));
//            System.out.println(jSONObject.get("upDir"));
            System.out.println(serverInfoBean.getIp()+"-----"+serverInfoBean.getUpDir());
        }
    }
    
    @After
    public void after() {
        System.out.println("After 单元测试结束");
    }
    
}