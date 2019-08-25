package com.web.controller;


import com.web.utils.InitDB;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
//@RestSchema(schemaId = "pageController")
@RequestMapping("/")
@EnableAutoConfiguration
public class PagesController {

    public static String userUrl;
    public static String productUrl;
    public static String shoppingcarUrl;
    public static String recordUrl;
    public static String evaluationUrl;

    public PagesController() {
        InitDB.connmysql();
        System.out.println("数据库初始化");
        this.userUrl = "cse://user-vmall/user";
        this.productUrl = "cse://product-vmall/product";
        this.shoppingcarUrl = "cse://shoppingcar-vmall/shoppingcar";
        this.recordUrl = "cse://record-vmall/record";
        this.evaluationUrl = "cse://evaluation-vmall/evaluation";
        System.out.println("url初始化：\n" + userUrl + "\n" + productUrl + "\n" + shoppingcarUrl + "\n" + recordUrl + "\n"+ evaluationUrl);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    String index() {
        return "main";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    String login() {
        return "login";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    String main() {
        return "main";
    }

    @RequestMapping(value = "/control", method = RequestMethod.GET)
    String control() {
        return "control";
    }

    @RequestMapping(value = "/amend_info", method = RequestMethod.GET)
    String amend_info() {
        return "amend_info";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(String searchKeyWord, HttpSession httpSession) {

        httpSession.setAttribute("searchKeyWord",searchKeyWord);
        System.out.println("searchKeyWord="+ searchKeyWord);
        return "search";
    }

    @RequestMapping(value = "/product_detail", method = RequestMethod.GET)
    String product_detail() {
        return "product_detail";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    String register() {
        return "register";
    }


    @RequestMapping(value = "/shopping_car", method = RequestMethod.GET)
    String shopping_car() {

        return "shopping_car";
    }

    @RequestMapping(value = "/shopping_handle", method = RequestMethod.GET)
    String shopping_handle() {

        return "shopping_handle";
    }
    @RequestMapping(value = "/shopping_record", method = RequestMethod.GET)
    String shopping_record() {

        return "shopping_record";
    }

    @RequestMapping(value = "/resetDb", method = RequestMethod.GET)
    @ResponseBody
    String  resetDb() {
        System.out.println("****resetDb****");
//		//初始化数据库
        String resultString = InitDB.connmysql();
        return resultString;
    }


}
