package com.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.utils.ArgsBean;

import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.client.RestTemplate;

@Controller
@EnableAutoConfiguration
public class RecordController {

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    @RequestMapping(value = "/addShoppingRecord",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addShoppingRecord(int userId,int productId,int counts){
        String url = PagesController.recordUrl+"/addShoppingRecord";
        Map<String,String> map = new HashMap<String,String>();
        map.put("userId", String.valueOf(userId));
        map.put("productId", String.valueOf(productId));
        map.put("counts", String.valueOf(counts));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n"+res);
        Map resultMap = (Map)JSON.parse(res);
        return resultMap;

    }

    @RequestMapping(value = "/changeShoppingRecord",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> changeShoppingRecord(int userId,int productId,String time,int orderStatus){
        String url = PagesController.recordUrl+"/changeShoppingRecord";
        Map<String,String> map = new HashMap<String,String>();
        map.put("userId", String.valueOf(userId));
        map.put("productId", String.valueOf(productId));
        map.put("time", time);
        map.put("orderStatus", String.valueOf(orderStatus));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n"+res);
        Map resultMap = (Map)JSON.parse(res);
        return resultMap;

    }

    @RequestMapping(value = "/getShoppingRecords",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getShoppingRecords(int userId){
        String url = PagesController.recordUrl+"/getShoppingRecords";
        Map<String,String> map = new HashMap<String,String>();
        map.put("userId", String.valueOf(userId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n"+res);
        Map resultMap = (Map)JSON.parse(res);
        return resultMap;
    }

    @RequestMapping(value = "/getShoppingRecordsByOrderStatus",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getShoppingRecordsByOrderStatus(int orderStatus){
        String url = PagesController.recordUrl+"/getShoppingRecordsByOrderStatus";
        Map<String,String> map = new HashMap<String,String>();
        map.put("orderStatus", String.valueOf(orderStatus));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n"+res);
        Map resultMap = (Map)JSON.parse(res);
        return resultMap;
    }

    @RequestMapping(value = "/getAllShoppingRecords",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getAllShoppingRecords(){
        String url = PagesController.recordUrl+"/getAllShoppingRecords";
        Map<String,String> map = new HashMap<String,String>();

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n"+res);
        Map resultMap = (Map)JSON.parse(res);
        return resultMap;
    }

    @RequestMapping(value = "/getUserProductRecord",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getUserProductRecord(int userId,int productId){
        String url = PagesController.recordUrl+"/getUserProductRecord";
        Map<String,String> map = new HashMap<String,String>();
        map.put("userId", String.valueOf(userId));
        map.put("productId", String.valueOf(productId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n"+res);
        Map resultMap = (Map)JSON.parse(res);
        return resultMap;


    }
}
