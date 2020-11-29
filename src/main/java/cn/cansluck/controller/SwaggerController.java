package cn.cansluck.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/swagger")
public class SwaggerController {

    @ApiOperation(value = "获取用户信息", notes = "根据Id来获取用户详情")
    @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "String")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> getInfo(@PathVariable String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "cansluck");
        map.put("age", 18);
        return map;
    }
}
