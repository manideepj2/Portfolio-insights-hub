package com.portfolio.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopHolding {
    private String instrument;
    private Double marketValue;
}