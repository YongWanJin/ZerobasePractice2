package zerobase.Weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration // API Document 작성용 클래스
//@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Weather Diary API Document")
                .description("Welecome to Weather Diary API Document")
                .version("1.0.0");
    }

//    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build().apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo(){
//        String description = "Welecome to Weather Diary API Document";
//        return new ApiInfoBuilder()
//                .title("DOCUMENT")
//                .description(description)
//                .version("1.0")
//                .build();
//    }
}
