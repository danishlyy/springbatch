package com.example.springbatch.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {

    private Long id;
    private String productName;
    private String productCode;
    private String productCompany;
}
