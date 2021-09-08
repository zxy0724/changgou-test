package zxy.changgou.pay.service.impl;

import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import zxy.changgou.pay.service.WxPayService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WXPay wxPay;
    @Value("${wxpay.notify_url}")
    private String notifyUrl;

    /**
     * 生成微信二维码
     *
     * @param orderId
     * @param money
     * @return
     */
    @Override
    public Map nativePay(String orderId, Integer money) {
        try {
            //1. 封装请求参数
            Map<String, String> map = new HashMap<>();
            map.put("body", "畅购");
            map.put("out_trade_no", orderId);

            BigDecimal payMoney = new BigDecimal("0.01");
            BigDecimal fen = payMoney.multiply(new BigDecimal("100")); //1.00
            fen = fen.setScale(0, BigDecimal.ROUND_UP); // 1
            map.put("total_fee", String.valueOf(fen));

            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url", notifyUrl);
            map.put("trade_type", "NATIVE");
            Map<String, String> mapResult = wxPay.unifiedOrder(map);//调用统一下单
            return mapResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    @Override
    public Map queryOrder(String orderId) {
        try {
            Map<String, String> map = new HashMap();
            map.put("out_trade_no", orderId);
            Map<String, String> orderQuery = wxPay.orderQuery(map);
            return orderQuery;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭订单
     *
     * @param orderId
     * @return
     */
    @Override
    public Map closeOrder(String orderId) {
        Map map = new HashMap();
        map.put("out_trade_no", orderId);
        try {
            return wxPay.closeOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
