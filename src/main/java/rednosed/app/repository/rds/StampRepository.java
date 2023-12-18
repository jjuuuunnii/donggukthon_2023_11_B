package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.Stamp;


@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {
}
