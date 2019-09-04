package com.product.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.product.controller.ProductController;
import com.product.entity.Product;
import com.product.repository.ProductRepository;
import com.product.utils.ArgsBean;
import com.product.utils.Response;

import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class  ProductService {

    @Autowired
    ProductRepository productRepository;

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    public Map<String, Object> getAllProduct(){
        Iterator<Product> iterator = productRepository.findAll().iterator();
        List<Product> list = new ArrayList<>();
        while (iterator.hasNext())
            list.add(iterator.next());

        String allProducts = JSONArray.toJSONString(list);
        System.out.println("我返回的结果是"+allProducts);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("allProducts", allProducts);
        return resultMap;
    }


    public Map<String, Object> updateProductCounts(ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String id = (String)map.get("id");
        String count = (String)map.get("count");
        Product product = productRepository.findOne(Integer.valueOf(id));
        product.setCounts(product.getCounts() - Integer.valueOf(count));
        productRepository.save(product);
        return null;
    }


    public  Response deleteProduct(ArgsBean argsBean){
         Map map = (Map) JSONObject.parse(argsBean.getMapString());
         //TODO 异常处理
         String id = (String)map.get("productId");

         try {
        	String url1 = ProductController.evaluationUrl+"/deleteEvaluationByProduct";
     		//String result1 = HttpUtil.sendGet(url1);
            String result1 = restTemplate.postForObject(url1,argsBean,String.class);

        	String url2 = ProductController.shoppingcarUrl+"/deleteShoppingCarByProduct";
     		String result2 = restTemplate.postForObject(url2,argsBean,String.class);

        	String url3 = ProductController.recordUrl+"/deleteShoppingRecordByProductId";
     		String result3 = restTemplate.postForObject(url3,argsBean,String.class);

             System.out.println("删除商品结果："+result1+"\n"+result2+"\n"+result3);
     		productRepository.delete(Integer.valueOf(id));
            return new Response(1, "删除商品成功", null);
        }catch (Exception e){
            return new Response(0,"删除商品失败",null);
        }
     }

     public Map<String, Object> addProduct(ArgsBean argsBean){
         Map map = (Map) JSONObject.parse(argsBean.getMapString());
         //TODO 异常处理
         String name = (String)map.get("name");
         String description = (String)map.get("description");
         String keyWord = (String)map.get("keyWord");
         String price = (String)map.get("price");
         String counts = (String)map.get("counts");
         String type = (String)map.get("type");

         System.out.println("添加了商品："+name);
         String result;

         Product product = new Product();
         product.setName(name);
         product.setDescription(description);
         product.setKeyWord(keyWord);
         product.setPrice(Integer.valueOf(price));
         product.setCounts(Integer.valueOf(counts));
         product.setType(Integer.valueOf(type));

         try {
             productRepository.save(product);
             result = "success";
         } catch (Exception e){
             result ="fail";
         }

         Map<String,Object> resultMap = new HashMap<String,Object>();
         resultMap.put("result",result);
         return resultMap;
     }

    public Product getProductById(ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String id = (String)map.get("productId");
        System.out.println("getProductById :"+id);
        return productRepository.findOne(Integer.valueOf(id));
    }
    public List<Product> getProductsByKeyWord(ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String searchKeyWord = (String)map.get("searchKeyWord");

        return productRepository.getProductsByKeyWord(searchKeyWord);
    }

    public Product getProductByName(ArgsBean argsBean){
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String name = (String)map.get("name");
        System.out.println("getProductByName :"+name);
        return productRepository.getProduct(name);
    }

    public boolean updateProduct(Product product){
        productRepository.save(product);
        return true;
    }

}
