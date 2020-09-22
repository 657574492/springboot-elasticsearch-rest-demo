package com.example.service;

import com.example.domain.Page;
import com.example.param.BookParam;
import com.example.vo.BookVo;

import java.io.IOException;

public interface BookService{

    Page<BookVo> bookSearch(BookParam param) throws IOException;


    Page<BookVo> searchALL();
}
