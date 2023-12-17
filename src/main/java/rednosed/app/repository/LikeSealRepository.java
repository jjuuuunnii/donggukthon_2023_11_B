package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.LikeSeal;

@Repository
public interface LikeSealRepository extends JpaRepository<LikeSeal, Long> {
}
