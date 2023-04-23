package com.Entities.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetTestResult {
    int count;
    List<Value> value;
}
