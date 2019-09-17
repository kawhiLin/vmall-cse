package com.product.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.product.entity.Product;

import com.product.service.ProductService;
import com.product.utils.ArgsBean;


import org.apache.servicecomb.provider.rest.common.RestSchema;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

import javax.servlet.http.*;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestSchema(schemaId = "product")
@RequestMapping(path = "/product", produces = MediaType.APPLICATION_JSON)
@EnableAutoConfiguration
public class ProductController extends HttpServlet {
    @Resource
    private ProductService productService;

    public static String userUrl;
    public static String productUrl;
    public static String shoppingcarUrl;
    public static String recordUrl;
    public static String evaluationUrl;

    public ProductController() {
        this.userUrl = "cse://use/user";
        this.productUrl = "cse://product/product";
        this.shoppingcarUrl = "cse://shoppingcart/shoppingcart";
        this.recordUrl = "cse://order/order";
        this.evaluationUrl = "cse://evaluation/evaluation";
        System.out.println("url初始化：\n" + userUrl + "\n" + productUrl + "\n" + shoppingcarUrl + "\n" + recordUrl + "\n"+ evaluationUrl);
    }
    
    @RequestMapping(value = "/getAllProducts", method = RequestMethod.POST)
    public String getAllProducts(){
        return JSONObject.toJSONString(productService.getAllProduct());
    }

    @RequestMapping(value = "/updateProductCounts", method = RequestMethod.POST)
    public String updateProductCounts(@RequestBody ArgsBean argsBean){
        return JSONObject.toJSONString(productService.updateProductCounts(argsBean));
    }



    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    public String deleteProduct(@RequestBody ArgsBean argsBean) {
        return JSONObject.toJSONString(productService.deleteProduct(argsBean));
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public String addProduct(@RequestBody ArgsBean argsBean) {
        return JSONObject.toJSONString(productService.addProduct(argsBean));
    }



    @RequestMapping(value = "/productDetail", method = RequestMethod.POST)
    public String productDetail(@RequestBody ArgsBean argsBean) {
        Product product = productService.getProductById(argsBean);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result","success");
        resultMap.put("productDetail",product);
        return JSONObject.toJSONString(resultMap);
    }


    @RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
    public String searchProduct(@RequestBody ArgsBean argsBean){
        List<Product> productList = new ArrayList<Product>();
        productList = productService.getProductsByKeyWord(argsBean);
        String searchResult = JSONArray.toJSONString(productList);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result",searchResult);
        System.out.println("我返回了"+searchResult);
        return JSONObject.toJSONString(resultMap);
    }

    @RequestMapping(value = "/getProductById", method = RequestMethod.POST)
    public String getProductById(@RequestBody ArgsBean argsBean) {
        Product product = productService.getProductById(argsBean);
        String result = JSON.toJSONString(product);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("result",result);
        System.out.println("getProductById return:"+JSONObject.toJSONString(resultMap));
        return JSONObject.toJSONString(resultMap);
    }

//    MultipartFile暂不支持
//    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
//    public String uploadFile(@RequestParam MultipartFile productImgUpload,String name, HttpServletRequest request) {
//        String result = "fail";
//        try{
//            if(productImgUpload != null && !productImgUpload.isEmpty()) {
//                String fileRealPath = request.getSession().getServletContext().getRealPath("/static/img");
//                int id = productService.getProductByName(name).getId();
//                String fileName = String.valueOf(id)+".jpg";
//                File fileFolder = new File(fileRealPath);
//                System.out.println("fileRealPath=" + fileRealPath+"/"+fileName);
//                if(!fileFolder.exists()){
//                    fileFolder.mkdirs();
//                }
//                File file = new File(fileFolder,fileName);
//                productImgUpload.transferTo(file);
//                result = "success";
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        Map<String,Object> resultMap = new HashMap<String,Object>();
//        resultMap.put("result",result);
//        return JSONObject.toJSONString(resultMap);
//    }

    //for 上传图片
    @RequestMapping(value = "/getProductIdByName", method = RequestMethod.POST)
    public String getProductIdByName(@RequestBody ArgsBean argsBean) {
    	 return new Integer(productService.getProductByName(argsBean).getId()).toString();
    }
    
    // for record调用
    @RequestMapping(value = "/updateProductById", method = RequestMethod.POST)
    public String updateProductById(@RequestBody ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String productJsonString = (String)map.get("productJsonString");
        System.out.println("updateProductById:"+productJsonString);
    	Product product=JSONObject.parseObject(productJsonString, Product.class);//JSON字符串转对象
        return new Boolean(productService.updateProduct(product)).toString();
    }
}
