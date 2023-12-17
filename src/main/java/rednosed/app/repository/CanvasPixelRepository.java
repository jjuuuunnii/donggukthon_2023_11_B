package rednosed.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rednosed.app.domain.CanvasPixel;

public interface CanvasPixelRepository extends JpaRepository<CanvasPixel, Long> {
}
