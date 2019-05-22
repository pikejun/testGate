package test.gate.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 16:02
 * @Description:
 */

@Entity(name = "gateway_route_definition")
@Data
public class GatewayRouteDefinition implements Serializable
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
   private String  id;
    @Column
    private String   filters;
    @Column
    private String   predicates;
    @Column
    private String  uri;

    public List<PredicateDefinition> getPredicateDefinition() {
        if (!StringUtils.isEmpty(predicates)) {
            Gson gson = new Gson();
            return gson.fromJson(this.predicates, new TypeToken<List<PredicateDefinition>>(){}.getType());
        } else {
            return null;
        }
    }
    public List<FilterDefinition> getFilterDefinition() {
        if (!StringUtils.isEmpty(filters)) {
            Gson gson = new Gson();
            return gson.fromJson(this.filters, new TypeToken<List<FilterDefinition>>(){}.getType());
        } else {
            return null;
        }
    }
}
