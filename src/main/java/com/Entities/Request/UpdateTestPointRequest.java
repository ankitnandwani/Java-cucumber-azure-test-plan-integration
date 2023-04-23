package com.Entities.Request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateTestPointRequest {
    private String outcome;
    private Boolean resetToActive;
}
