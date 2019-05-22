package test.gate.config;

import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 16:08
 * @Description:
 */
@Configuration
public class CoreConfiguration {

    @Bean
    public RouteDefinitionWriter routeDefinitionWriter() {
        return new InMemoryRouteDefinitionRepository();
    }

    @Bean
    public CustomRouteDefinitionLoad customRouteDefinitionLoad() {
        return new CustomRouteDefinitionLoad();
    }
}
