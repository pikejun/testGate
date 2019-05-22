package test.gate.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 16:02
 * @Description:
 */

@Entity(name = "gate_auth_ignore")
@Data
public class GateAuthIgnore implements Serializable
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
   private String  id;

    @Column
    private String   comment;

    @Column
    private String  url;
}
