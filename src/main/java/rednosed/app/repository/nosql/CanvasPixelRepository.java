package rednosed.app.repository.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import rednosed.app.domain.nosql.CanvasPixel;

public interface CanvasPixelRepository extends MongoRepository<CanvasPixel, String> {
}
