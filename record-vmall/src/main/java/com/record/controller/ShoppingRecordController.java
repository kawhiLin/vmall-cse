package com.record.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.record.entity.Product;
import com.record.entity.ShoppingRecord;


import com.record.service.ShoppingRecordService;


import com.record.utils.ArgsBean;
import io.swagger.models.auth.In;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;


import java.awt.print.PrinterGraphics;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestSchema(schemaId = "order")
@RequestMapping(path = "/order", produces = MediaType.APPLICATION_JSON)
@EnableAutoConfiguration
public class ShoppingRecordController {

	@Resource
	private ShoppingRecordService shoppingRecordService;

	private static RestTemplate restTemplate = RestTemplateBuilder.create();
	private boolean isFault = false;

	public static String userUrl;
	public static String productUrl;
	public static String shoppingcarUrl;
	public static String recordUrl;
	public static String evaluationUrl;
	private static String exporterUrl;

    public ShoppingRecordController() {
		this.userUrl = "cse://use/user";
		this.productUrl = "cse://product/product";
		this.shoppingcarUrl = "cse://shoppingcart/shoppingcart";
		this.recordUrl = "cse://order/order";
		this.evaluationUrl = "cse://evaluation/evaluation";
		this.exporterUrl = "http://127.0.0.1:8099/hello";
		System.out.println("url初始化：\n" + userUrl + "\n" + productUrl + "\n" + shoppingcarUrl + "\n" + recordUrl + "\n"+ evaluationUrl);
	}


	class MutliThread  implements Runnable{
		@Override
		public void run(){
			try {
				// 处理connect exporter异常
				String res = restTemplate.getForObject(exporterUrl,String.class);
				System.out.println("INFO ----add ordd, send to exporter. res:\n" + res);
			}catch (Exception e){
				System.out.println("ERROR ----add ordd, send to exporter failed!");
			}
		}

	}

	// 开启模拟故障，设置故障开关为true
	@RequestMapping(value = "/makeFault", method = RequestMethod.POST)
	public String makeFault() {
		isFault = true;
		System.out.println("make fault");
		return null;
	}

	// 恢复故障，设置故障开关为false
	@RequestMapping(value = "/stopFault", method = RequestMethod.POST)
	public String stopFault() {
		isFault = false;
		System.out.println("stop fault");
		return null;
	}
	// 健康检查接口
	@RequestMapping(value = "/healthCheck", method = RequestMethod.GET)
	public String healthCheck() {
		if (isFault){
			// 人为抛出异常
			if (isFault)   {
				System.out.println("[ERROR]Server error message is: Order Not Found");
//				throw new InvocationException(503, "", "Order Not Found");
				throw new RuntimeException("[ERROR]Server error message is [{\"message\":\"Order Not Found\"}].");
			}

		}
		return null;
	}


	@RequestMapping(value = "/addShoppingRecord", method = RequestMethod.POST)
	public String addShoppingRecord(@RequestBody ArgsBean argsBean) {
		// 统计add订单qps，请求到exporter 转移到子线程处理
		try {
			ExecutorService service = Executors.newFixedThreadPool(1);//TPSNum是线程数
			service.execute(new MutliThread());
			service.shutdown();
		}catch (Exception e) {
			System.out.println("service.execute(new MutliThread()) failed:" + e.getMessage());
		}

		return JSONObject.toJSONString(shoppingRecordService.addShoppingRecord(argsBean));
	}

	@RequestMapping(value = "/changeShoppingRecord", method = RequestMethod.POST)
	public String changeShoppingRecord(@RequestBody ArgsBean argsBean) {
    	//int userId, int productId, String time, int orderStatus
		Map map = (Map) JSONObject.parse(argsBean.getMapString());
		//TODO 异常处理
		String userId = (String)map.get("userId");
		String productId = (String)map.get("productId");
		String time = (String)map.get("time");
		String orderStatus = (String)map.get("orderStatus");

		System.out.println("我接收了" + userId + " " + productId + " " + time + " " + orderStatus);
		ShoppingRecord shoppingRecord = shoppingRecordService.getShoppingRecord(Integer.valueOf(userId), Integer.valueOf(productId), time);
		System.out.println("我获取到了了" + shoppingRecord.getTime());
		shoppingRecord.setOrderStatus(Integer.valueOf(orderStatus));
		shoppingRecordService.updateShoppingRecord(shoppingRecord);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "success");
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/getShoppingRecords", method = RequestMethod.POST)
	public String getShoppingRecords(@RequestBody ArgsBean argsBean) throws InterruptedException {
		//if (isFault) throw new RuntimeException("[ERROR]Server error message is [{\"message\":\"Order Not Found\"}].");
		if (isFault)   {
			System.out.println("[ERROR]Server error message is: Order Not Found");
			//Thread.sleep(1000);
			// 490 for 熔断  503 for 容错
			throw new InvocationException(503, "", "Order Not Found");
			//throw new RuntimeException("[ERROR]Server error message is [{\"message\":\"Order Not Found\"}].");
		}

		Map map = (Map) JSONObject.parse(argsBean.getMapString());
		//TODO 异常处理
		String userId = (String)map.get("userId");

		List<ShoppingRecord> shoppingRecordList = shoppingRecordService.getShoppingRecords(Integer.valueOf(userId));
		//默认最多显示15条记录
		if (shoppingRecordList.size()>=15){
			shoppingRecordList =  shoppingRecordList.subList(0,15);
		}

		String shoppingRecords = JSONArray.toJSONString(shoppingRecordList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", shoppingRecords);
		System.out.println("res="+JSONObject.toJSONString(resultMap));
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/getShoppingRecordsByOrderStatus", method = RequestMethod.POST)
	public String getShoppingRecordsByOrderStatus(@RequestBody ArgsBean argsBean) {
		Map map = (Map) JSONObject.parse(argsBean.getMapString());
		//TODO 异常处理
		String orderStatus = (String)map.get("orderStatus");

		List<ShoppingRecord> shoppingRecordList = shoppingRecordService.getShoppingRecordsByOrderStatus(Integer.valueOf(orderStatus));
		String shoppingRecords = JSONArray.toJSONString(shoppingRecordList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", shoppingRecords);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/getAllShoppingRecords", method = RequestMethod.POST)
	public String getAllShoppingRecords() throws InterruptedException {
		//if (isFault) throw new RuntimeException("[ERROR]Server error message is [{\"message\":\"Order Not Found\"}].");
		if (isFault)   {
			System.out.println("[ERROR]Server error message is: Order Not Found");
			// 404 无法验证容错
//			Thread.sleep(1000);
			// 490 for 熔断  503 for 容错
			throw new InvocationException(490, "", "Order Not Found");
//			throw new RuntimeException("[ERROR]Server error message is [{\"message\":\"Order Not Found\"}].");
		}
		List<ShoppingRecord> shoppingRecordList = shoppingRecordService.getAllShoppingRecords();
		String shoppingRecords = JSONArray.toJSONString(shoppingRecordList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", shoppingRecords);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/getUserProductRecord", method = RequestMethod.POST)
	public String getUserProductRecord(@RequestBody ArgsBean argsBean) {
		Map map = (Map) JSONObject.parse(argsBean.getMapString());
		//TODO 异常处理
		String userId = (String)map.get("userId");
		String productId = (String)map.get("productId");

		String result = "false";
		if (shoppingRecordService.getUserProductRecord(Integer.valueOf(userId), Integer.valueOf(productId))) {
			result = "true";
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return JSONObject.toJSONString(resultMap);
	}

	// 供user应用调用
	@RequestMapping(value = "/deleteShoppingRecordByUser", method = RequestMethod.POST)
	public String deleteShoppingRecordByUser(@RequestBody ArgsBean argsBean) {
		Map map = (Map) JSONObject.parse(argsBean.getMapString());
		//TODO 异常处理
		String userId = (String)map.get("userId");

		String result = "false";
		if (shoppingRecordService.deleteShoppingRecordByUser(Integer.valueOf(userId))) {
			result = "true";
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return JSONObject.toJSONString(resultMap);
	}

	// 供product应用调用
	@RequestMapping(value = "/deleteShoppingRecordByProductId", method = RequestMethod.POST)
	public String deleteShoppingRecordByProductId(@RequestBody ArgsBean argsBean) {
		Map map = (Map) JSONObject.parse(argsBean.getMapString());
		//TODO 异常处理
		String productId = (String)map.get("productId");

		String result = "false";
		if (shoppingRecordService.deleteShoppingRecordByProductId(Integer.valueOf(productId))) {
			result = "true";
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", result);
		return JSONObject.toJSONString(resultMap);
	}
}
