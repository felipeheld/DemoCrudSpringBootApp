package com.felipeheld.grades.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    
	  @Bean
      public Docket api() {                
          return new Docket(DocumentationType.SWAGGER_2)          
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.felipeheld.grades.api.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
      }
      
      private ApiInfo apiInfo() {
          return new ApiInfo(
            "Nota API", 
            "API para criação, deleção e busca de notas de alunos por disciplina.", 
            "v1", 
            "...", 
            new Contact("Felipe Held Izquierdo", "https://github.com/felipeheld", "felipeheld@hotmail.com"), 
            "...", 
            "...", 
            Collections.emptyList());
      }
  
      @Override
      public void addViewControllers(ViewControllerRegistry registry) {
          registry.addRedirectViewController("/", "/swagger-ui/");
      }
}
