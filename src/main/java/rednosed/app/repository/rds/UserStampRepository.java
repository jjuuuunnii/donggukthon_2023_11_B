package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.UserStamp;

@Repository
public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
}
