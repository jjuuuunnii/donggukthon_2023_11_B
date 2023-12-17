package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.LikeStamp;


@Repository
public interface LikeStampRepository extends JpaRepository<LikeStamp, Long> {
}
