package com.testCase.testCase.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "currency")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    private String type; //dollars or euros
    private String source; // kaynak1 or kaynak2
    private String date;
    private double buy;
    private double sell;



}
