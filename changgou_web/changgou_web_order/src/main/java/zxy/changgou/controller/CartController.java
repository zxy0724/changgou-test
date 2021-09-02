package zxy.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zxy.changgou.feign.CartFeign;
import zxy.changgou.user.feign.AddressFeign;

import java.util.Map;

@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    private CartFeign cartFeign;

    /**
     * 查询
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model){
        Map map = cartFeign.list();
        model.addAttribute("items",map);
        return "cart";
    }

    /**
     * 添加购物车
     * @param id
     * @param num
     * @return
     */
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(String id, Integer num){

        cartFeign.add(id, num);

        Map map = cartFeign.list();
        return new Result<>(true, StatusCode.OK,"添加购物车成功",map);
    }

}
