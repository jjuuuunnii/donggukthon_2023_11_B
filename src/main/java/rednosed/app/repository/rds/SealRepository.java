package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.Seal;

@Repository
public interface SealRepository extends JpaRepository<Seal, Long> {
}
