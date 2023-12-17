package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.UserStamp;

@Repository
public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
}
