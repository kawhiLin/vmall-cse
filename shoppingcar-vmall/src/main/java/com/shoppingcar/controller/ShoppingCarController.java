package com.shoppingcar.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shoppingcar.entity.Product;

import com.shoppingcar.entity.ShoppingCar;

import com.shoppingcar.service.ShoppingCarService;


import com.shoppingcar.utils.ArgsBean;
import io.swagger.models.auth.In;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestSchema(schemaId = "shoppingcar")
@RequestMapping(path = "/shoppingcar", produces = MediaType.APPLICATION_JSON)
@EnableAutoConfiguration
public class ShoppingCarController {

    @Resource
    private ShoppingCarService shoppingCarService;

    public static String userUrl;
    public static String productUrl;
    public static String shoppingcarUrl;
    public static String recordUrl;
    public static String evaluationUrl;

    RestTemplate restTemplate = RestTemplateBuilder.create();

    public ShoppingCarController() {
        this.userUrl = "cse://use/user";
        this.productUrl = "cse://product/product";
        this.shoppingcarUrl = "cse://shoppingcar/shoppingcar";
        this.recordUrl = "cse://order/order";
        this.evaluationUrl = "cse://evaluation/evaluation";
        System.out.println("url初始化：\n" + userUrl + "\n" + productUrl + "\n" + shoppingcarUrl + "\n" + recordUrl + "\n"+ evaluationUrl);
    }


    
    @RequestMapping(value = "/addShoppingCar",method = RequestMethod.POST)
    public String addShoppingCar(@RequestBody ArgsBean argsBean) {
            //(int userId,int productId,int counts){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userId = (String)map.get("userId");
        String productId = (String)map.get("productId");
        String counts = (String)map.get("counts");

        System.out.println("数量为"+counts);
        ShoppingCar shoppingCar = shoppingCarService.getShoppingCar(Integer.valueOf(userId),Integer.valueOf(productId));
        
        //请求product应用，get product
        String url1 = this.productUrl+"/getProductById";
//		String result1 = HttpUtil.sendPost(url1, reqMap);
        String result1 = restTemplate.postForObject(url1,argsBean,String.class);


		System.out.println("---------------result1:\n"+result1);
		Map maps = (Map)JSON.parse(result1);
	    String productJsonString = (String)maps.get("result");
		Product product=JSONObject.parseObject(productJsonString, Product.class);//JSON字符串转对象
		
        if(shoppingCar == null){
            ShoppingCar shoppingCar1 = new ShoppingCar();
            shoppingCar1.setUserId(Integer.valueOf(userId));
            shoppingCar1.setProductId(Integer.valueOf(productId));
            shoppingCar1.setCounts(Integer.valueOf(counts));
            
           // shoppingCar1.setProductPrice(productService.getProduct(productId).getPrice()*counts);
    		shoppingCar1.setProductPrice(product.getPrice()*Integer.valueOf(counts));
    		
            shoppingCarService.addShoppingCar(shoppingCar1);
        }
        else{
            shoppingCar.setCounts(shoppingCar.getCounts()+Integer.valueOf(counts));
            //shoppingCar.setProductPrice(productService.getProduct(productId).getPrice()*shoppingCar.getCounts());
            shoppingCar.setProductPrice(product.getPrice()*shoppingCar.getCounts());
            
            shoppingCarService.updateShoppingCar(shoppingCar);
        }
        Map<String, Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result","success");
        // for页面显示不同版本的购物车，增加version字段
        resultMap.put("version","1.0");
        // resultMap.put("version","2.0");
        System.out.println("我返回了");
        return JSONObject.toJSONString(resultMap);
    }

    @RequestMapping(value = "/getShoppingCars",method = RequestMethod.POST)
    public String getShoppingCars(@RequestBody ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userId = (String)map.get("userId");

        List<ShoppingCar> shoppingCarList = shoppingCarService.getShoppingCars(Integer.valueOf(userId));
        String shoppingCars = JSONArray.toJSONString(shoppingCarList);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result",shoppingCars);
        return JSONObject.toJSONString(resultMap);
    }

    @RequestMapping(value = "/deleteShoppingCar",method = RequestMethod.POST)
    public String deleteShoppingCar(@RequestBody ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userId = (String)map.get("userId");
        String productId = (String)map.get("productId");

        shoppingCarService.deleteShoppingCar(Integer.valueOf(userId),Integer.valueOf(productId));
        Map<String, Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result","success");
        return JSONObject.toJSONString(resultMap);
    }
    
    
    //供user应用调用
    @RequestMapping(value = "/deleteShoppingCarByUser",method = RequestMethod.POST)
    public String deleteShoppingCarByUser(@RequestBody ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userId = (String)map.get("userId");

        String result = "false";
        if(shoppingCarService.deleteShoppingCarByUser(Integer.valueOf(userId))){
            result = "true";
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result",result);
        return JSONObject.toJSONString(resultMap);
    }
    
    
    //供product应用调用
    @RequestMapping(value = "/deleteShoppingCarByProduct",method = RequestMethod.POST)
    public String deleteShoppingCarByProduct(@RequestBody ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String productId = (String)map.get("productId");

        String result = "false";
        if(shoppingCarService.deleteShoppingCarByProduct(Integer.valueOf(productId))){
            result = "true";
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result",result);
        return JSONObject.toJSONString(resultMap);
    }
    
}
