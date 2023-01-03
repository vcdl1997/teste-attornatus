package br.com.testeattornatus.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigurations {
    
    @Bean
    public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(apiInfo())
			.select()
            .apis(RequestHandlerSelectors.basePackage("br.com.testeattornatus"))
            .paths(PathSelectors.any())
            .build()
         ;
    }
    
    private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
		    .title("REST API - Teste Attornatus")
		    .description("Aplicação Spring Boot REST API criada para fins de avaliação técnica")
		    .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .termsOfServiceUrl("")
            .version("1.0.0")
		    .contact(new Contact("Vinicius", "https://www.linkedin.com/in/vinicius-costa-de-lima/", "viniciuslima.dev@outlook.com"))
		    .build()
		;
    }
}
