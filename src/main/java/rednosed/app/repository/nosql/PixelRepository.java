package rednosed.app.repository.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import rednosed.app.domain.nosql.Pixel;

public interface PixelRepository extends MongoRepository<Pixel, String> {
}
