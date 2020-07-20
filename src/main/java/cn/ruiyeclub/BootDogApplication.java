package cn.ruiyeclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class BootDogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootDogApplication.class, args);
    }
}
