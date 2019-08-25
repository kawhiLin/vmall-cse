package com.record.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.record.controller.ShoppingRecordController;
import com.record.entity.Product;
import com.record.entity.ShoppingRecord;
import com.record.repository.RecordRepository;
import com.record.utils.ArgsBean;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShoppingRecordService {
    @Autowired
    RecordRepository recordRepository;

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    @Transactional
    public Map<String, Object> addShoppingRecord(ArgsBean argsBean){
        //int userId, int productId, int counts
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userId = (String)map.get("userId");
        String productId = (String)map.get("productId");
        String counts = (String)map.get("counts");

        System.out.println("我添加了" + userId + " " + productId);
        String result = null;

        // Product product = productService.getProduct(productId);
        //String result1 = HttpUtil.sendPost(url1, reqMap);
        String url1 = ShoppingRecordController.productUrl+"/getProductById";
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("productId", productId);

        ArgsBean argsBean1 = new ArgsBean();
        argsBean1.setMapString(JSONObject.toJSONString(map1));
        String result1 = restTemplate.postForObject(url1,argsBean1,String.class);

        System.out.println("---------------result1:\n" + result1);
        Map maps = (Map) JSON.parse(result1);
        String productJsonString = (String) maps.get("result");
        Product product = JSONObject.parseObject(productJsonString, Product.class);// JSON字符串转对象

        if (Integer.valueOf(counts) <= product.getCounts()) {
            ShoppingRecord shoppingRecord = new ShoppingRecord();
            shoppingRecord.setUserId(Integer.valueOf(userId));
            shoppingRecord.setProductId(Integer.valueOf(productId));
            shoppingRecord.setProductPrice(product.getPrice() * Integer.valueOf(counts));
            shoppingRecord.setCounts(Integer.valueOf(counts));
            shoppingRecord.setOrderStatus(0);
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            shoppingRecord.setTime(sf.format(date));
            product.setCounts(product.getCounts() - Integer.valueOf(counts));
            // productService.updateProduct(product);
            String url2 = ShoppingRecordController.productUrl+"/updateProductById";
            Map<String, String> map2 = new HashMap<String, String>();
            String productJsonString2 = JSON.toJSONString(product);
            map2.put("productJsonString", productJsonString2);

            //String result2 = HttpUtil.sendPost(url2, map2);
            //String result2 = restTemplate.postForObject(url2,map2,String.class);
            ArgsBean argsBean2 = new ArgsBean();
            argsBean2.setMapString(JSONObject.toJSONString(map2));
            System.out.println("argsBean2:"+argsBean2.getMapString());
            String result2 = restTemplate.postForObject(url2,argsBean2,String.class);

            System.out.println("result2:" + result2);

            recordRepository.save(shoppingRecord);
            result = "success";
        } else {
            result = "unEnough";
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return resultMap;
    }

    public ShoppingRecord getShoppingRecord(int userId, int productId,String time){
        return  recordRepository.getShoppingRecord(userId, productId,time);
    }

    @Transactional
    public boolean updateShoppingRecord(ShoppingRecord shoppingRecord){
        recordRepository.save(shoppingRecord);
        return true;
    }

   public List<ShoppingRecord> getShoppingRecordsByOrderStatus(int orderStatus){
        return recordRepository.getShoppingRecordsByOrderStatus(orderStatus);
   }

    public List<ShoppingRecord> getShoppingRecords(int userId){
        return recordRepository.getShoppingRecords(userId);
    }

    public List<ShoppingRecord> getAllShoppingRecords(){
        Iterator<ShoppingRecord> it = recordRepository.findAll().iterator();
        List<ShoppingRecord> list = new ArrayList<>();
        while (it.hasNext()){
            list.add(it.next());
        }
        return list;
    }

    public boolean getUserProductRecord(int userId,int productId){
        if(recordRepository.getUserProductRecord(userId,productId).size()>0){
            return true;
        }
        return false;
    }
    @Transactional
    public  boolean deleteShoppingRecordByUser(int userId){
        recordRepository.deleteShoppingRecordByUser(userId);
        return true;
    }
    @Transactional
    public  boolean deleteShoppingRecordByProductId(int productId){
        recordRepository.deleteShoppingRecordByProductId(productId);
        return true;
    }
}
