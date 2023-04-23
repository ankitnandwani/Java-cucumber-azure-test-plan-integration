package com.backend;

public interface Constants {
    String AZURE_API = "AZURE_API";
    String AZURE_PAT = "csjv43d4tyseeh5pfbl7cal5hggt5p53zfb6hy2awiaalqxrxmkq";
    String PROJECT = "test";
    String ORGANIZATION = "ankitnandwani";
    String TEST_PLAN_ID = "1";
    String TEST_SUITE_ID = "2";

    String CREATE_TEST_RUN = "/{organization}/{project}/_apis/test/runs?api-version=7.0";
    String UPDATE_TEST_RUN = "/{organization}/{project}/_apis/test/runs/{runId}?api-version=7.0";
    String TEST_RESULT = "/{organization}/{project}/_apis/test/Runs/{runId}/results?api-version=7.0";
}
