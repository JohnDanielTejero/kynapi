package com.kyn.api.kynapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.kyn.api.kynapi.Config.AppConfig;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class KynapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KynapiApplication.class, args);
	}

}
