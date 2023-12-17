package rednosed.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.service.StampService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stamp")
public class StampController {

    private final StampService stampService;

    @GetMapping("/stamp-list")
    public ResponseDto<?> showUserSealList
}
