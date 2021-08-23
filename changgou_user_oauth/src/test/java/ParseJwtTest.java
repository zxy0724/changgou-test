import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {
    /***
     * 校验令牌
     */
    @Test
    public void testParseToKen(){
        //令牌
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.ajk7cLOEccQeehNp188U8hDiJfJqpPAxqX0h3eUP-0OP3Ny-3bJeulzgx9j7t-zQohYA_rmeTSprMi1LB5Ehl116Q1B7KoSGxcOnpHMZtoNwYp73mGb9KIsmCiEteC2d38FF7yQoU3_0H95ZK0QATkd0u8moJQ4XOJbtLmu8N3yrTuKPWGISLCdOeQkk4NyA2nfVkFxiZH2_baIcN35JXqggIdHJfCm2EyuHkmTJlKRbT7HU61UItdCjVXZne1jsc6Jqdt6XLYybEL8BY2KauvSsNHRq8UBabxNb0DironZjwiaSICqnW0iMLlH5sQsIT3Zfdbe486XBjI5EVNiduQ";
        //公钥
        String publickey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv/3GVKXCxXY9cmlwrLO/Osa51hsJoA/+sbk9hpQDUR70MzJdTNwb4BywrEZrwHlirFJMYi3DjF1m0bnminVLtjkvjOClbZbTqsmpJe2uynhoMtDlwNh0BzoReJAMt3EjJ5iHGSjp/e1yET1UPNq+LPzyl1eZjXsC7fTrX9MFg0UTQZ2S6OSX7dWukN6KLdFQtn6Fzn/C1gpq3VV12DZ76tJB2nJa/2P4MYdfUQvIJ5s3IUG+p8FeA1/kzpzgQoKAuDxBGK/kXyMFbFwnDNTn2REbi+DizsYP3tB1vjLZWwwIqa8bnGWwYqFqn9xcIp1Y60pJdPVQRCQLrvS4USCSDQIDAQAB-----END PUBLIC KEY-----";
        //检验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        //获取jwt的原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
