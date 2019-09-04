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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.client.RestTemplate;

@Controller
@EnableAutoConfiguration
public class RecordController {

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    private boolean isTestingTPS = false;

    class MutliThread  implements Runnable{
        @Override
        public void run(){
            //这里实现http请求
            System.out.println("http request send!");
            addShoppingRecord(1,1,1);
        }
    }

// 压测
    @RequestMapping(value = "/startTestTPS",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> testTPS(int TPSNum){
        isTestingTPS = true;
        int j = 0;
        while (isTestingTPS){
            try {
                ExecutorService service = Executors.newFixedThreadPool(TPSNum);//TPSNum是线程数
                for (int i = 0; i < TPSNum && isTestingTPS; i++){
                    System.out.println(j);
                    j ++;
                    service.execute(new MutliThread());
                }
                service.shutdown();
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    @RequestMapping(value = "/stopTestTPS",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> stopTestTPS(){
        isTestingTPS = false;
        System.out.println("stop test");
        return null;

    }

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

        //删除购物车记录
//        ShoppingcarController shoppingcarController = new ShoppingcarController();
//        shoppingcarController.deleteShoppingCar(userId,productId);

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
