package com.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.user.controller.UserController;
import com.user.entity.User;
import com.user.entity.UserDetail;
import com.user.repository.UserDetailRepository;
import com.user.repository.UserRepository;
import com.user.utils.ArgsBean;
import com.user.utils.Response;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
import java.text.SimpleDateFormat;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;

    private static RestTemplate restTemplate = RestTemplateBuilder.create();

    public Map<String, Object> doLogin(ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userNameOrEmail = (String)map.get("userNameOrEmail");
        String password = (String)map.get("password");

        System.out.println("我接收到了登录请求" + userNameOrEmail + " " + password);
        String result = "fail";
        User user = userRepository.getUserByNameOrEmail(userNameOrEmail);
        if (user == null)
            result = "unexist";
        else {
            UserDetail userDetail = userDetailRepository.findOne(user.getId());
            if (userDetail.getPassword().equals(password)) {
                result = "success";
            } else
                result = "wrong";
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        resultMap.put("currentUser", user);
        return resultMap;
    }

    @Transactional
    public Map<String, Object> doRegister(ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userName = (String)map.get("userName");
        String email = (String)map.get("email");
        String nickName = (String)map.get("nickName");
        String address = (String)map.get("address");
        String birthday = (String)map.get("birthday");
        String password = (String)map.get("password");
        String phoneNumber = (String)map.get("phoneNumber");
        String sex = (String)map.get("sex");
        String postNumber = (String)map.get("postNumber");

        String result = "fail";
        //这里name和email逻辑有问题
        User user = userRepository.getUserByNameOrEmail(userName);
        if (user != null) {
            result = "nameExist";
        } else {
            user = userRepository.getUserByNameOrEmail(email);
            if (user != null)
                result = "emailExist";
            else {
                User user1 = new User();
                user1.setName(userName);
                System.out.println(userName);
                user1.setEmail(email);
                System.out.println(email);
                user1.setNickName(nickName);
                System.out.println(nickName);
                user1.setRole(0);
                userRepository.save(user1);
                user1 = userRepository.getUserByNameOrEmail(userName);
                UserDetail userDetail = new UserDetail();
                userDetail.setId(user1.getId());
                userDetail.setAddress(address);
                userDetail.setBirthday(birthday);
                userDetail.setPassword(password);
                userDetail.setPhoneNumber(phoneNumber);
                userDetail.setSex(Integer.valueOf(sex));
                userDetail.setPostNumber(postNumber);
                Date date = new Date();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userDetail.setRegisterTime(sf.format(date));
                userDetailRepository.save(userDetail);
                result = "success";
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return resultMap;
    }


    @Transactional
    public Map<String, Object> doUpdate(ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String userName = (String)map.get("userName");
        String email = (String)map.get("email");
        String nickName = (String)map.get("nickName");
        String address = (String)map.get("address");
        String birthday = (String)map.get("birthday");
        String password = (String)map.get("password");
        String phoneNumber = (String)map.get("phoneNumber");
        String sex = (String)map.get("sex");
        String postNumber = (String)map.get("postNumber");


        String result = "fail";
        User user = userRepository.getUserByNameOrEmail(userName);
        user.setEmail(email);
        user.setNickName(nickName);
        userRepository.save(user);
        UserDetail userDetail = userDetailRepository.findOne(user.getId());
        userDetail.setAddress(address);
        userDetail.setBirthday(birthday);
        userDetail.setPassword(password);
        userDetail.setPhoneNumber(phoneNumber);
        userDetail.setSex(Integer.valueOf(sex));
        userDetail.setPostNumber(postNumber);
        userDetailRepository.save(userDetail);
        result = "success";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return resultMap;
    }

    public Map<String, Object> getAllUser() {
        Iterator<User> userIt = userRepository.findAll().iterator();
        List<User> userList = new ArrayList<>();
        while (userIt.hasNext())
            userList.add(userIt.next());

        String allUsers = JSONArray.toJSONString(userList);
        System.out.println("我返回的结果是"+allUsers);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("allUsers", allUsers);
        return resultMap;
    }

    @Transactional
    public Response deleteUser(ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String id = (String)map.get("id");
        map.put("userId",id);
        argsBean.setMapString(JSONObject.toJSONString(map));

        try {

            //evaluationDao.deleteEvaluationByUser(id);
            String url1 = UserController.evaluationUrl+"/deleteEvaluationByUser";
            //String result1 = HttpUtil.sendGet(url1);
            String result1 = restTemplate.postForObject(url1,argsBean,String.class);

            //shoppingCarDao.deleteShoppingCarByUser(id);
            String url2 = UserController.shoppingcarUrl+"/deleteShoppingCarByUser";
            String result2 = restTemplate.postForObject(url2,argsBean,String.class);


            //shoppingRecordDao.deleteShoppingRecordByUser(id);
            String url3 = UserController.recordUrl+"/deleteShoppingRecordByUser";
            String result3 = restTemplate.postForObject(url3,argsBean,String.class);

            System.out.println("删除用户结果："+result1+"\n"+result2+"\n"+result3);

            userDetailRepository.delete(Integer.valueOf(id));
            userRepository.delete(Integer.valueOf(id));
            return new Response(1, "删除用户成功", null);
        }catch (Exception e){
            return new Response(0,"删除用户失败",null);
        }
    }

    public Map<String, Object> getUserAddressAndPhoneNumber(ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String id = (String)map.get("id");

        UserDetail userDetail = userDetailRepository.findOne(Integer.valueOf(id));
        String address = userDetail.getAddress();
        String phoneNumber = userDetail.getPhoneNumber();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("address", address);
        resultMap.put("phoneNumber", phoneNumber);
        return resultMap;
    }

    public Map<String, Object> getUserById(ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String id = (String)map.get("id");
        System.out.println("id is "+id);
        User user = userRepository.findOne(Integer.valueOf(id));
        String result = JSON.toJSONString(user);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return resultMap;
    }

    public Map<String, Object> getUserDetailById(ArgsBean argsBean) {
        Map map = (Map) JSONObject.parse(argsBean.getMapString());
        //TODO 异常处理
        String id = (String)map.get("id");

        UserDetail userDetail = userDetailRepository.findOne(Integer.valueOf(id));
        String result = JSON.toJSONString(userDetail);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        return resultMap;
    }
}
