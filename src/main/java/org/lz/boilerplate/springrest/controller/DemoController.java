package org.lz.boilerplate.springrest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/demo")
public class DemoController {

    public static final String HELLOWORLD = "helloworld";

    @GetMapping("hello")
    public String hello(){
        return HELLOWORLD;
    }

    @GetMapping("secure/hello")
    public String secureHello(){
        return HELLOWORLD;
    }
}
