package com.cafevery.controller;


import com.cafevery.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    @PostMapping("/api/v1/cafe/make-cafe-data")
    public void makeCafeData() {
        cafeService.makeLocationOfCafeSync();
    }

    @GetMapping("/api/v1/cafe/test-cafe-info-url")
    public void testCafeInfoUrl() {
        cafeService.getCafeBusinessHour();
    }
}
