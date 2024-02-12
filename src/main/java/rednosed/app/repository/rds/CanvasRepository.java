package rednosed.app.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.rds.Canvas;
import rednosed.app.domain.rds.User;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CanvasRepository extends JpaRepository<Canvas, Long> {
    Optional<Canvas> findByCanvasClientId(String canvasClientId);

    Optional<Canvas> findByRoomMaker(User user);

    void deleteByRoomMaker(User user);

}
