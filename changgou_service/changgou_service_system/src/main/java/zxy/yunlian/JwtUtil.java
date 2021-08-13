package zxy.yunlian;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {
    //一小时
    public static final Long JWT_TTL = 3600000L;
    //秘钥
    public static final String JWT_KEY = "itcast";

    /**
     * 创建token
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder().setId(id).setSubject(subject).setIssuer("admin").setIssuedAt(now).signWith(signatureAlgorithm, secretKey).setExpiration(expDate);
        return builder.compact();
    }
    public static SecretKey generalKey(){
        byte[] encodeKey= Base64.getDecoder().decode(JWT_KEY);
        SecretKey key=new SecretKeySpec(encodeKey,0,encodeKey.length,"AES");
        return key;
    }
}
