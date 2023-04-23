package com.Entities.Request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateTestRun {
    private String name;
    private ShallowReference plan;
    private List<Integer> pointIds;
    private String state;
}
