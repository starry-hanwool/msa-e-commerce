package me.hanwool.coupon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class CouponApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CouponApplication.class, args);
		String mongoDBHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoDBPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		log.info("Connected to MongoDB - {} : {}", mongoDBHost, mongoDBPort);
	}

}
