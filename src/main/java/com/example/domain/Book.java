package com.example.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Book {


    private String author;

    private String title;

    private Integer wordCount;

    private Date publishDate;


}
