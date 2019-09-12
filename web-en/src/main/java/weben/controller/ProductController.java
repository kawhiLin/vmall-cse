package weben.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import weben.entity.Product;
import weben.utils.ArgsBean;
import weben.utils.Response;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@EnableAutoConfiguration
public class ProductController {

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getAllProducts() {
        String url =  PagesController.productUrl+"/getAllProducts";

        String res = restTemplate.postForObject(url,null,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;
    }


    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    @ResponseBody
    public Response deleteProduct(int productId) {
        String url = PagesController.productUrl+"/deleteProduct";
        Map<String, String> map = new HashMap<String, String>();
        map.put("productId", String.valueOf(productId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        JSONObject productJsonString = JSONObject.parseObject(res);
        Response response = JSONObject.toJavaObject(productJsonString, Response.class);
        return response;
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(String name, String description, String keyWord, int price, int counts,
                                          int type) {
        String url =  PagesController.productUrl+"/addProduct";
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("description", description);
        map.put("keyWord", keyWord);
        map.put("price", String.valueOf(price));
        map.put("counts", String.valueOf(counts));
        map.put("type", String.valueOf(type));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;
    }

    @RequestMapping(value = "/productDetail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> productDetail(int productId, HttpSession httpSession) {
        String url = PagesController.productUrl+"/productDetail";
        Map<String, String> map = new HashMap<String, String>();
        map.put("productId", String.valueOf(productId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        String result = (String) resultMap.get("result");
        if (result.equals("success")) {
            JSONObject userJsonString = (JSONObject) resultMap.get("productDetail");
            Product product = JSONObject.toJavaObject(userJsonString, Product.class);

            httpSession.setAttribute("productDetail", product);
        }
        return resultMap;
    }


    @RequestMapping(value = "/searchPre", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchPre(String searchKeyWord, HttpSession httpSession) {
        httpSession.setAttribute("searchKeyWord", searchKeyWord);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", "success");
        return resultMap;
    }



    @RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchProduct(String searchKeyWord) {
        String url = PagesController.productUrl+"/searchProduct";
        Map<String, String> map = new HashMap<String, String>();
        map.put("searchKeyWord", searchKeyWord);

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;
    }

    @RequestMapping(value = "/getProductById", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getProductById(int productId) {
        String url =  PagesController.productUrl+"/getProductById";
        Map<String, String> map = new HashMap<String, String>();
        map.put("productId", String.valueOf(productId));

        ArgsBean argsBean = new ArgsBean();
        argsBean.setMapString(JSONObject.toJSONString(map));
        String res = restTemplate.postForObject(url,argsBean,String.class);

        System.out.println("----res:\n" + res);
        Map resultMap = (Map) JSON.parse(res);
        return resultMap;

    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam MultipartFile productImgUpload, String name,
                                          HttpServletRequest request) {
        String result = "fail";
        try {
            if (productImgUpload != null && !productImgUpload.isEmpty()) {
                String fileRealPath = request.getSession().getServletContext().getRealPath("/static/img");
                //int id = productService.getProduct(name).getId();
                //获取id
                String url =  PagesController.productUrl+"/getProductIdByName";
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);

                ArgsBean argsBean = new ArgsBean();
                argsBean.setMapString(JSONObject.toJSONString(map));
                String res = restTemplate.postForObject(url,argsBean,String.class);

                System.out.println("----id res:\n" + res);
                int id = new Integer(res);


                String fileName = String.valueOf(id) + ".png";
                File fileFolder = new File(fileRealPath);
                System.out.println("fileRealPath=" + fileRealPath + "/" + fileName);
                if (!fileFolder.exists()) {
                    fileFolder.mkdirs();
                }
                File file = new File(fileFolder, fileName);
                productImgUpload.transferTo(file);
                result = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return resultMap;
    }

}
