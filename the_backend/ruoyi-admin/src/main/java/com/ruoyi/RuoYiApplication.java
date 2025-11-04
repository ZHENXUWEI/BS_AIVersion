package com.ruoyi;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ForestScan(basePackages = "com.ruoyi.web")
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // 禁用DevTools自动重启（可选，根据需要开启）
        System.setProperty("spring.devtools.restart.enabled", "false");

        // 只启动一次应用，并获取环境对象
        SpringApplication app = new SpringApplication(RuoYiApplication.class);
        ConfigurableEnvironment env = app.run(args).getEnvironment();

        // 打印激活的配置文件和配置路径
        System.out.println("激活的环境：" + Arrays.toString(env.getActiveProfiles()));
        System.out.println("配置文件位置：" + env.getProperty("spring.config.location"));
        System.out.println("(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}