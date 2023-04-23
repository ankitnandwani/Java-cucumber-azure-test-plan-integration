package com.Entities.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTestRunResponse {
    int id;
    String name;
}
