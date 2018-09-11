package com.lj.main;

import com.lj.autoRemote.service.AutoRemoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Web小工具启动类 **/
@SpringBootApplication
@ComponentScan(basePackages={"com.lj"})
public class WebAutoRemoteMain implements CommandLineRunner {
	
	private static Logger logger = Logger.getLogger(WebAutoRemoteMain.class);

    @Value("${spring.profiles.active}")
    String active;
    @Value("${server.port}")
    String port;

    @Autowired
    private AutoRemoteService autoRemoteService;

    @Override
    public void run(String... args) throws Exception {
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdft.format(new Date());
        System.out.println(time+" spring.profiles.active:"+active+",port:"+port);
        autoRemoteService.startServer();
        System.out.println(time+" WebAutoRemoteMain run is OK! ^_^");
    }

    public static void main(String[] args) {
        SpringApplication.run(WebAutoRemoteMain.class, args);
        logger.info("WebAutoRemoteMain run is OK! ^_^");
    }

}
