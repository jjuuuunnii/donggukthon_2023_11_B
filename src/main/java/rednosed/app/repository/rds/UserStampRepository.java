package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.Stamp;
import rednosed.app.domain.rds.User;
import rednosed.app.domain.rds.UserStamp;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
    @Query("SELECT us FROM UserStamp us JOIN FETCH us.stamp WHERE us.user = :user")
    List<UserStamp> findByUser(@Param("user") User user);

    @Query("SELECT u.nickname FROM UserStamp us JOIN us.user u WHERE us.stamp = :stamp")
    List<String> findUserNicknamesByStamp(@Param("stamp") Stamp stamp);

    List<UserStamp> findByStamp(Stamp stamp);

}
