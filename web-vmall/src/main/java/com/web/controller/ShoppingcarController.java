package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.web.utils.ArgsBean;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import java.util.Map;

@Controller
@EnableAutoConfiguration
public class ShoppingcarController {

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    @RequestMapping(value = "/addShoppingCar",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addShoppingCar(int userId,int productId,int counts){
        String url =  PagesController.shoppingcarUrl+"/addShoppingCar";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));
        map.put("productId", String.valueOf(productId));
        map.put("counts", String.valueOf(counts));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;
    }

    @RequestMapping(value = "/getShoppingCars",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getShoppingCars(int userId){
        String url = PagesController.shoppingcarUrl+"/getShoppingCars";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;

    }

    @RequestMapping(value = "/deleteShoppingCar",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteShoppingCar(int userId,int productId){
        String url = PagesController.shoppingcarUrl+"/deleteShoppingCar";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));
        map.put("productId", String.valueOf(productId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;
    }
}
