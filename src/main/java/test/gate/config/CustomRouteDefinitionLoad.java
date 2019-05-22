package test.gate.config;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import test.gate.domain.GatewayRouteDefinition;
import test.gate.jpa.GatewayRouteDefinitionRepository;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 16:11
 * @Description:
 */

public class CustomRouteDefinitionLoad implements RouteDefinitionRepository {
    @Autowired
    private GatewayRouteDefinitionRepository gatewayRouteDefinitionRepository;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            List<GatewayRouteDefinition> customRouteDefinitions = gatewayRouteDefinitionRepository.findAll();
            Map<String, RouteDefinition> routes = new LinkedHashMap<>();
            for (GatewayRouteDefinition customRouteDefinition: customRouteDefinitions) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(customRouteDefinition.getId());
                definition.setUri(new URI(customRouteDefinition.getUri()));
                List<PredicateDefinition> predicateDefinitions = customRouteDefinition.getPredicateDefinition();
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }
                List<FilterDefinition> filterDefinitions = customRouteDefinition.getFilterDefinition();
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }
                routes.put(definition.getId(), definition);
            }
            return Flux.fromIterable(routes.values());
        } catch (Exception e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            try {
                Gson gson = new Gson();
                GatewayRouteDefinition customRouteDefinition = new GatewayRouteDefinition();
                customRouteDefinition.setId(r.getId());
                customRouteDefinition.setUri(r.getUri().toString());
                customRouteDefinition.setPredicates(gson.toJson(r.getPredicates()));
                customRouteDefinition.setFilters(gson.toJson(r.getFilters()));
                gatewayRouteDefinitionRepository.save(customRouteDefinition);
                return Mono.empty();
            } catch (Exception e) {
                e.printStackTrace();
                return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition save error: "+ r.getId())));
            }
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            try {
                gatewayRouteDefinitionRepository.deleteById(id);
                return Mono.empty();
            } catch (Exception e) {
                e.printStackTrace();
                return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition delete error: " + routeId)));
            }
        });
    }
}
