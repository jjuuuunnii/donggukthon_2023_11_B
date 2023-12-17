package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.Canvas;

@Repository
public interface CanvasRepository extends JpaRepository<Canvas, Long> {

}
