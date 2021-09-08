package zxy.changgou.pay.service;

import java.util.Map;

public interface WxPayService {
    /**
     * 生成微信二维码
     * @param orderId
     * @param money
     * @return
     */
    Map nativePay(String orderId,Integer money);

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    Map queryOrder(String orderId);
    /**
     * 关闭订单
     * @param orderId
     * @return
     */
    Map closeOrder(String orderId);
}
