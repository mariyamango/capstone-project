package com.example.backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record WorkSummaryResponseDto(
        List<WorkCountdownDto> workCountdowns,
        Map<String, BigDecimal> totalByType,
        BigDecimal grandTotal
) {
}
