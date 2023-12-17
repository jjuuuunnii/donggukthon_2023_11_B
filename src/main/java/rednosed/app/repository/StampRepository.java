package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.Stamp;


@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {
}
