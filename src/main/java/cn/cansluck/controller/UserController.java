package cn.cansluck.controller;

import cn.cansluck.model.User;
import cn.cansluck.service.IUserService;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;

    @RequestMapping("/hello")
    public String hello() {
        logger.debug("这是一个hello日志");
        return "hello spring-boot";
    }

    @RequestMapping("/login")
    public String login(String username, String password) {
        boolean isLogin = iUserService.login(username, password);
        if (isLogin) {
            return "登陆成功";
        } else {
            return "登陆失败";
        }
    }

    @RequestMapping("/register")
    public String register(String username, String password) {
        boolean isLogin = iUserService.register(username, password);
        if (isLogin) {
            return "注册成功";
        } else {
            return "注册失败";
        }
    }

    @RequestMapping("/batchAdd")
    public String batchAdd(String username, String password) {
        iUserService.batchAdd(username, password);
        return "成功";
    }

    @RequestMapping("/getUserList")
    public String getUserList(String username) {
        List<User> userList = iUserService.getUserList(username);
        return JSONArray.toJSONString(userList);
    }
}
