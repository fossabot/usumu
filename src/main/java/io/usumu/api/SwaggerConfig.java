package io.usumu.api;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@PropertySource("classpath:swagger.properties")
@Configuration
@EnableSwagger2
@ComponentScan("io.usumu.api.subscription.controller")
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.any())
                    .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .apiInfo(metadata())
                .forCodeGeneration(true)
                .tags(
                        new Tag(
                                "Subscriptions",
                                "Create, verify, list, retreive or delete subscriptions."
                        ),
                        new Tag(
                                "Logs",
                                "Retreive transaction logs for a certain subscriber (email, phone)."
                        ),
                        new Tag(
                                "Newsletter",
                                "Manage and send out newsletters."
                        ),
                        new Tag(
                                "Metadata",
                                "Store metadata for subscribers."
                        )
                )
                .useDefaultResponseMessages(false)
                ;
    }

    public static ApiInfo metadata(){
        return new ApiInfoBuilder()
                .title("Usumu API")
                .description("Usumu is a self-hosted newsletter subscription microservice. It contains no authentication or authorization features, its only job is to store subscriber data securely.")
                .version("1.x")
                .build();
    }
}
