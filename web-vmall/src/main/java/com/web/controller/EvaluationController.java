package com.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.utils.ArgsBean;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Controller
@EnableAutoConfiguration
public class EvaluationController {

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    @RequestMapping(value = "/addShoppingEvaluation",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addShoppingEvaluation(int userId, int productId, String content){
        String url = PagesController.evaluationUrl+"/addShoppingEvaluation";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", String.valueOf(userId));
        map.put("productId", String.valueOf(productId));
        map.put("content", content);

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;
    }

    @RequestMapping(value = "/getShoppingEvaluations",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getShoppingEvaluations(int productId){
        String url = PagesController.evaluationUrl+"/getShoppingEvaluations";
        Map<String, String> map = new HashMap<String, String>();
        map.put("productId", String.valueOf(productId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;

    }
}
