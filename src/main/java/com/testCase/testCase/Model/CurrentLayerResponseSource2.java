package com.testCase.testCase.Model;


import lombok.Getter;

import java.util.Map;

@Getter
public class CurrentLayerResponseSource2 {


    public boolean success;
    public int timestamp;
    public String base;
    public String date;
    public Map<String, Double> rates;

}

