package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rednosed.app.repository.rds.StampRepository;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;
}
