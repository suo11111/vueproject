package com.example.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;


@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    private String issuer;//签发者
    private String secret;//密钥
    private Integer expiration;//过期时间

    /*@param map 自定义参数*/
    public String generateToken(Map<String, String> map) {
        Calendar instance = Calendar.getInstance();//设置令牌过期时间
        instance.add(Calendar.MINUTE, expiration);//设置失效时间
        JWTCreator.Builder builder = JWT.create();//创建jwt builder
        //playload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        String token = builder.withIssuer(issuer).withIssuedAt(new Date())//设置签发者
                .withExpiresAt(instance.getTime())//设置过期时间
                .sign(Algorithm.HMAC256(secret));
        return token;
    }

    /*是否合法*/
    public boolean verify(String token) {
        try {
           JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }
    /*解析token*/
    public DecodedJWT jwtDecode(String token){
        try{
            return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        }catch (SignatureVerificationException e){
            throw new RuntimeException("签名错误");
        } catch (AlgorithmMismatchException e){
            throw new RuntimeException("算法不匹配");
        } catch (TokenExpiredException e){
            throw new RuntimeException("令牌过期");
        } catch (Exception e){
            throw new RuntimeException("令牌解析失败");
        }
    }

}
