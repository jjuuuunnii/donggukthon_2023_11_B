package rednosed.app.repository.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rednosed.app.domain.nosql.Pixel;

import java.util.Optional;

@Repository
public interface PixelRepository extends MongoRepository<Pixel, String> {
    Optional<Pixel> findByCanvasClientId(String canvasClientId);

    void deleteAllByCanvasClientId(String canvasClientId);
}
