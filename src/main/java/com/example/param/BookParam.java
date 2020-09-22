package com.example.param;

import com.example.domain.Book;
import com.example.domain.Page;
import lombok.Data;

import java.util.Date;

@Data
public class BookParam extends Page {

    private String author;

    private String title;

    private Integer wordCount;

    private Date publishDate;

    private String startDate;

    private String endDate;
}
