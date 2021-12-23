package top.moflowerlkh.shareparkingdemo.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    private ApiInfo ApiInfo() {
        return new ApiInfoBuilder()
                .title("Share-Parking-demo")
                .description("共享停车系统的基本接口")
                .version("1.0")
                .build();
    }
}
