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
        if (null == username || "".equals(username))
            return "login";
        boolean isLogin = userService.login(username, password);
        if (isLogin) {
            map.addAttribute("username", username);
            return "chat";
        }
        return "login";
    }
}