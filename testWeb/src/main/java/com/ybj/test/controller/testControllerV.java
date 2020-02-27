package com.ybj.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author testController
 * @Description //TODO $
 * @Date $ $
 * @Param $
 * @return $
 **/
@RestController
public class testControllerV {

    @RequestMapping("/test1")
    public String test(){
        return "hello";
    }
}
