package test.gate.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import test.gate.domain.GateAuthIgnore;

/**
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 16:06
 * @Description:
 */
public interface GateAuthIgnoreRepository extends JpaRepository<GateAuthIgnore,String> {
}
