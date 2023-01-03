package br.com.testeattornatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
@SpringBootApplication
public class TesteAttornatusApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesteAttornatusApplication.class, args);
	}

}
