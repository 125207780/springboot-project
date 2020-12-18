package cn.cansluck.controller;

import cn.cansluck.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录页
 *
 * @author Cansluck
 */
@RequestMapping("/chat")
@Controller
public class ChatController {

    @Autowired
    private IUserService userService;

    /**
     * 登陆
     *
     * @author Cansluck
     * @return 返回页面
     */
    @RequestMapping("/login")
    public String login(String username, String password, ModelMap map) {
        // 判断是否为空
        if (null == username || "".equals(username))
            // 为空则留在登陆页面
            return "login";
        // 根据输入的用户名和密码查询用户信息
        boolean isLogin = userService.login(username, password);
        // 判断是否存在该用户
        if (isLogin) {
            map.addAttribute("username", username);
            // 存在则进入聊天室
            return "chat";
        }
        return "login";
    }
}