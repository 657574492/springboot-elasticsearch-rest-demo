package com.example.controller;

import com.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest")
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping(value = "/searchbook", method = RequestMethod.GET)
    public String test() {
        bookService.bookSearch();
        return "success";
    }

}
