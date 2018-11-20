package com.demo.controller;

import com.demo.annotation.IgnoreLogin;
import com.demo.annotation.LoginUser;
import com.demo.bean.Business;
import com.demo.bean.User;
import com.demo.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/login")
    @IgnoreLogin
    public String login() {
        //在此 我们不做登录检验 假设检验成功

        User user = new User();
        user.setUserId(9527);
        user.setUserName("小星星");
        return jwtUtils.generateToken(user);//这里只是为了测试只返回token,(请求不含IgnoreLogin注解时需要将token放在头信息里)
    }

    @PostMapping("/business")
    public User business(@RequestBody Business business, @LoginUser User user) {//这里可以直接使用注解使用user的信息了
        logger.info("用户信息参数id:{},姓名:{}", user.getUserId(), user.getUserName());
        logger.info("我们的业务参数:{},{}", business.getStr(), business.getNum());
        return user;
    }
}
