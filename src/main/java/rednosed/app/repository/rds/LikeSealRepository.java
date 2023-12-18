package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.LikeSeal;
import rednosed.app.domain.rds.User;

import java.util.List;

@Repository
public interface LikeSealRepository extends JpaRepository<LikeSeal, Long> {
    List<LikeSeal> findByUser(User user);
}
