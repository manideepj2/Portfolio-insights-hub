package com.portfolio.app.controller;

import com.portfolio.app.dto.HoldingResponse;
import com.portfolio.app.service.HoldingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investor")
@RequiredArgsConstructor
public class HoldingsController {

    private final HoldingsService holdingsService;

    @GetMapping("/{investorId}/holdings")
    public List<HoldingResponse> getHoldings(
            @PathVariable String investorId) {

        return holdingsService.getHoldings(investorId);
    }
}