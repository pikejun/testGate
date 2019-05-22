package test.gate.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import test.gate.domain.GateAuthIgnore;
import test.gate.jpa.GateAuthIgnoreRepository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 16:15
 * @Description:
 */
@Component
@Order(1)
public class TokenGlobalFilter implements GlobalFilter {

    public static  String AUTHENTICATION_KEY="AUTHENTICATION";

    public static  String AUTHENTICATION_USER="AUTHENTICATION_USER";

    @Autowired
    private GateAuthIgnoreRepository gateAuthIgnoreRepository;

    @PostConstruct
    public void init(){
        List<GateAuthIgnore> authIgnoreUrlList = gateAuthIgnoreRepository.findAll();
        authIgnoreUrlList.forEach(authIgnoreUrl -> ContextVariables.addIgnoreUrl(authIgnoreUrl.getUrl()));
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String uri = exchange.getRequest().getPath().pathWithinApplication().value();
        // 不需要验证token的url
        if(ContextVariables.containsIgnoreUrl(uri)){
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst(AUTHENTICATION_KEY);
        // 支持从查询请求中获取参数
        if(StringUtils.isEmpty(token) && exchange.getRequest().getMethod()== HttpMethod.GET){
            token = exchange.getRequest().getQueryParams().getFirst(AUTHENTICATION_KEY);
        }
        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String userId=token;
        ServerHttpRequest host = exchange.getRequest().mutate().header(AUTHENTICATION_USER,userId).header(AUTHENTICATION_KEY,token).build();
        //将现在的request 变成 change对象
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }
}