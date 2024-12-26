package com.testCase.testCase.Model;

import lombok.Getter;

import java.util.Map;

@Getter
public class CurrentLayerResponse {

    private boolean success;
    private String source;
    private String timestamp;
    private Map<String, Double> quotes;

}
