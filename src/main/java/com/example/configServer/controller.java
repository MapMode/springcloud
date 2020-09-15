package com.example.configServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class controller {

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "hello world";
    }

}
