package zxy.changgou.filter;

public class UrlFilter {
    //所有需要传递令牌的地址
    public static String filterPath="/api/wseckillorder,/api/seckill,/api/wxpay,/api/wxpay/**,/api/worder/**,/api/user/**,/api/address/**,/api/wcart/**,/api/cart/**,/api/categoryReport/**,/api/orderConfig/**,/api/order/**,/api/orderItem/**,/api/orderLog/**,/api/preferential/**,/api/returnCause/**,/api/returnOrder/**,/api/returnOrderItem/**";
    public static boolean hasAuthorize(String url){
        String[] split = filterPath.replace("**","").split(",");
        for (String value : split) {
            if(url.startsWith(value)){
                return true;
            }
        }
        return false;
    }
}
