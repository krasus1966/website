package top.krasus1966.website.controller;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Krasus1966
 * @date 2020/4/4 23:10
 **/
public class IndexController {


    @GetMapping("/")
    public String homeIndex(){

        return "index";
    }
}
