package com.website.WalletService.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class ApiDocConfig {

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Matt Freedman", "https://www.jointheleague.org", "mjfre@aol.com");
        return new ApiInfoBuilder()
                .title("Wallet Service")
                .description("Spring Boot API that stores and distributes Thor wallet addresses, given a Terra wallet address")
                .contact(contact)
                .build();
    }

    @Bean
    public Docket api() {
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey("JWT", HttpHeaders.AUTHORIZATION, "header"));

        List<SecurityContext> contextList = new ArrayList<>();
        contextList.add(securityContext());


        return new Docket(DocumentationType.SWAGGER_2)
                .produces(Collections.singleton("application/json"))
                .consumes(Collections.singleton("application/json"))
                .securitySchemes(schemeList)
                .securityContexts(contextList)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.website.WalletService.presentation"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
    }

}
