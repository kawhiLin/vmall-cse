package com.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.user.service.UserService;
import com.user.utils.ArgsBean;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

@RestSchema(schemaId = "user")
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON)
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    public static String userUrl;
    public static String productUrl;
    public static String shoppingcarUrl;
    public static String recordUrl;
    public static String evaluationUrl;

    public UserController() {
        this.userUrl = "cse://user-vmall/user";
        this.productUrl = "cse://product-vmall/product";
        this.shoppingcarUrl = "cse://shoppingcar-vmall/shoppingcar";
        this.recordUrl = "cse://record-vmall/record";
        this.evaluationUrl = "cse://evaluation-vmall/evaluation";
        System.out.println("url初始化：\n" + userUrl + "\n" + productUrl + "\n" + shoppingcarUrl + "\n" + recordUrl + "\n"+ evaluationUrl);
    }

    @RequestMapping(value = "/doLogin", produces = { "application/json" }, method = RequestMethod.POST)
    public String doLogin(@RequestBody ArgsBean argsBean) {
        return JSONObject.toJSONString(userService.doLogin(argsBean));
    }

    @RequestMapping(value = "/doRegister", method = RequestMethod.POST)
    public String  doRegister(@RequestBody ArgsBean argsBean) {
        System.out.println("我接收到了注册用户请求");
        return JSONObject.toJSONString(userService.doRegister(argsBean));
    }

    @RequestMapping(value = "/doUpdate", method = RequestMethod.POST)
    public String doUpdate(@RequestBody ArgsBean argsBean) {
        System.out.println("我接收到了更新用户请求");
        return JSONObject.toJSONString(userService.doUpdate(argsBean));
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.POST)
    public String getAllUser() {
        System.out.println("我接收到了获取用户请求");
        return JSONObject.toJSONString(userService.getAllUser());
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestBody ArgsBean argsBean) {
        return  JSONObject.toJSONString(userService.deleteUser(argsBean));
    }

    @RequestMapping(value = "/getUserAddressAndPhoneNumber", method = RequestMethod.POST)
    public String getUserAddressAndPhoneNumber(@RequestBody ArgsBean argsBean) {
        return JSONObject.toJSONString(userService.getUserAddressAndPhoneNumber(argsBean));
    }

    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    public String getUserById(@RequestBody ArgsBean argsBean){
        System.out.println("我接收到了getUserById请求");
        return JSONObject.toJSONString(userService.getUserById(argsBean));
    }

    @RequestMapping(value = "/getUserDetailById", method = RequestMethod.POST)
    public String getUserDetailById(@RequestBody ArgsBean argsBean) {
        return JSONObject.toJSONString(userService.getUserDetailById(argsBean));
    }
}
