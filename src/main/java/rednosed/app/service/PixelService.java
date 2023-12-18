package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rednosed.app.repository.nosql.PixelRepository;

@Service
@RequiredArgsConstructor
public class PixelService {

    private final PixelRepository pixelRepository;

}
