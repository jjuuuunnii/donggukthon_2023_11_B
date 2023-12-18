package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.Canvas;

@Repository
public interface CanvasRepository extends JpaRepository<Canvas, Long> {

}
