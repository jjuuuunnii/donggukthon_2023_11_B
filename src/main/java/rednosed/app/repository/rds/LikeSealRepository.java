package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.LikeSeal;

@Repository
public interface LikeSealRepository extends JpaRepository<LikeSeal, Long> {
}
