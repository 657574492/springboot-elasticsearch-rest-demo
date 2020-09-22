package com.example.controller;

import com.example.domain.Page;
import com.example.domain.Result;
import com.example.domain.StatusCode;
import com.example.param.BookParam;
import com.example.service.BookService;
import com.example.vo.BookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping(value = "/searchBook", method = RequestMethod.GET)
    public Result<BookVo> searchBook(BookParam param) throws IOException {
        Page<BookVo> page = bookService.bookSearch(param);
        return new Result<BookVo>(true, StatusCode.OK,"查询成功",page) ;
    }


}
