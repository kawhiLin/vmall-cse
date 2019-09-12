package vmall.newArrivals2.controller;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;

@RestSchema(schemaId = "newArrivals")
@RequestMapping(path = "/newArrivals", produces = MediaType.APPLICATION_JSON)
@EnableAutoConfiguration
public class newArrivals2Controller {

    //供product应用调用
    @RequestMapping(value = "/getNewArrivals",method = RequestMethod.GET)
    public String getNewArrivals(@RequestParam String username){
        String version = "v2.0";
        System.out.println("version="+version+",username="+username);
        return version;
    }
}
