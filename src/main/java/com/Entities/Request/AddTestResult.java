package com.Entities.Request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddTestResult {
    private String testCaseTitle;
    private String automatedTestName;
    private Integer priority;
    private String outcome;
    private ShallowReference testRun;
    private ShallowReference testCase;
    private ShallowReference testPoint;
    private Integer testCaseRevision;
    private Integer id;

}
