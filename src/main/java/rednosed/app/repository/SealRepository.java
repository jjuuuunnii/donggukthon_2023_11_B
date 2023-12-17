package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.Seal;

@Repository
public interface SealRepository extends JpaRepository<Seal, Long> {
}
