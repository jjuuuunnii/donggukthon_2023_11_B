package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.User;
import rednosed.app.domain.rds.UserStamp;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
    List<UserStamp> findByUser(User user);


}
