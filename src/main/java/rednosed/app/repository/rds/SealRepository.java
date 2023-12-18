package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.Seal;
import rednosed.app.domain.rds.User;

import java.util.List;

@Repository
public interface SealRepository extends JpaRepository<Seal, Long> {

    List<Seal> findByUser(User user);

}
