package com.example.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Page<T> implements Serializable {


    private int currentPage = 1;

    private int pageSize = 10;

    private long totalCount;

    private List<T> list;
}
