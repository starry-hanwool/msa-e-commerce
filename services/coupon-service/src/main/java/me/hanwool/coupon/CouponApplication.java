package me.hanwool.coupon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan("me.hanwool")
public class CouponApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CouponApplication.class, args);
		String mongoDBHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoDBPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
		log.info("Coupon Service Connected to MongoDB - {} : {}", mongoDBHost, mongoDBPort);
	}

}
