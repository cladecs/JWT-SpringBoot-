package com.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.demo.bean.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private long   expire;
    private String secret;
    private String header;

    /**
     * 生成jwt token
     */
    public String generateToken(User user) {
        Date nowDate = new Date();

        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(JSONObject.toJSONString(user))
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析出来claim
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * 得到user
     * @param claims
     * @return
     */
    public User getUser(Claims claims) {
        return JSONObject.parseObject(claims.getSubject(), User.class);
    }

    /**
     * token是否过期
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public static void main(String[] agrs) {
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setExpire(604800);
        jwtUtils.setSecret("f4e2e52034348f86b67cde581c0f9eb5");
        jwtUtils.setHeader("token");
        User user = new User();
        user.setUserId(9527);
        user.setUserName("小星星");
        String token = jwtUtils.generateToken(user);
        System.out.println(token);

        User user1 = JSONObject.parseObject(jwtUtils.getClaimByToken(token).getSubject(), User.class);
        System.out.println(user1.getUserId());
    }
}
