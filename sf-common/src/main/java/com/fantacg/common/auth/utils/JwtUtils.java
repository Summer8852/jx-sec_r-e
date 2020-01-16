package com.fantacg.common.auth.utils;

import com.fantacg.common.auth.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname JwtUtils
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class JwtUtils {


    /**
     * 公钥解析Token
     * @param publicKey
     * @param token
     * @return
     */
    private  Jws<Claims> parseToken(PublicKey publicKey, String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }


    /**
     * 公钥解析Token
     * @param publicKey
     * @param token
     * @return
     * @throws Exception
     */
    private  Jws<Claims> parseToken(byte[] publicKey, String token) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtils.getPublicKey(publicKey)).parseClaimsJws(token);
    }


    /**
     * 从Token中获取用户信息（使用公钥解析）
     * @param publicKey
     * @param token
     * @return
     * @throws Exception
     */
    public UserInfo getUserInfo(byte[] publicKey, String token) throws Exception {
        Jws<Claims> claimsJws = parseToken(publicKey, token);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstans.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstans.JWT_KEY_USER_NAME))
        );
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, String roles,PrivateKey privateKey, int expireMinutes) {
        return Jwts.builder()
                .claim(JwtConstans.JWT_KEY_ID, id)
                .claim(JwtConstans.JWT_KEY_USER_NAME, subject)
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .claim("roles", roles)
                .compact();
    }

    /**
     * 解析JWT(使用公钥解析）
     * @return
     */
    public Claims parseJWT(byte[] publicKey, String token) throws Exception{
        Jws<Claims> claimsJws = parseToken(publicKey, token);
        return claimsJws.getBody();
    }

    /**
     * 从Token中获取用户信息（使用公钥解析）
     * @param publicKey
     * @param token
     * @return
     */
    public  Claims parseJWT(PublicKey publicKey, String token) {
        Jws<Claims> claimsJws = parseToken(publicKey, token);
        return claimsJws.getBody();
    }
}
