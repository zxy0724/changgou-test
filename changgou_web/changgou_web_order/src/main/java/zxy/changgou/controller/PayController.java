package zxy.changgou.controller;

import com.changgou.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zxy.changgou.order.feign.OrderFeign;
import zxy.changgou.pay.feign.WxPayFeign;
import zxy.changgou.pojo.Order;

import java.util.Map;

@Controller
@RequestMapping("/wxpay")
public class PayController {
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private WxPayFeign wxPayFeign;

    /**
     * 微信支付二维码
     * @param orderId
     * @return
     */
    @GetMapping
    public String wxPay(String orderId, Model model) {
        //根据id查询订单
        Result<Order> orderResult = orderFeign.findById(orderId);
        if (orderResult.getData() == null) {
            //不存在则返回出错页
            return "fail";
        }
        Order order = orderResult.getData();
        //判断支付状态
        if (!"0".equals(order.getPayStatus())) {
            //如果不是未支付订单返回
            return "fail";
        }
        //3.基于payFeign调用统一下单接口,并获取返回结果
        Result payResult = wxPayFeign.nativePay(orderId,order.getPayMoney());
        if(payResult.getData()==null){
            return "fail";
        }
        //4.封装结果数据
        Map payMap = (Map) payResult.getData();
        payMap.put("orderId",orderId);
        payMap.put("payMoney",order.getPayMoney());
        model.addAllAttributes(payMap);
        return "wxpay";
    }
    //支付成功页面的跳转
    @RequestMapping("/toPaySuccess")
    public String toPaySuccess(Integer payMoney,Model model){
        model.addAttribute("payMoney",payMoney);
        return "paysuccess";
    }
}
