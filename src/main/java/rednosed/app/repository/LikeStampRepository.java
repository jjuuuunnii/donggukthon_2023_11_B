package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.LikeStamp;


@Repository
public interface LikeStampRepository extends JpaRepository<LikeStamp, Long> {
}
