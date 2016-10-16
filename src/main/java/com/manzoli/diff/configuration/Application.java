package com.manzoli.diff.configuration;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Spring Boot main class. This class is called to initiate the Spring Boot Application itself.
 *
 */
@SpringBootApplication(scanBasePackages = "com.manzoli.diff")
@EnableSwagger2
public class Application {

	/** The main method to start the Spring Boot Application */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket diffApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("diff")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/diff.*"))
                .build()
                .pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Diff scalable web project")
                .description("Composed by 3 endpoints, two to receive a JSON base64 encoded for the left/right sides and one to return the result of the diff between the JSON's.")
                .contact(new Contact("Jose Luiz Manzoli", null, "josemanzoli@gmail.com"))
                .version("2.0")
                .build();
    }
}
