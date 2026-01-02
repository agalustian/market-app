package ru.market.shopfront;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MarketAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketAppApplication.class, args);
	}

}
