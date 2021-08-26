package zxy.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zxy.changgou.config.TokenDecode;
import zxy.changgou.dao.TaskMapper;
import zxy.changgou.service.CartService;

import java.util.Map;

@RestController
//@CrossOrigin
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private TokenDecode tokenDecode;

    @GetMapping("/add")
    public Result add(@RequestParam("skuId") String skuId,@RequestParam("num")Integer num){
        //静态获取
//        String username="changgou";
        //获取用户名
        String username=tokenDecode.getUserInfo().get("username");
        cartService.add(skuId,num,username);
        return new Result(true, StatusCode.OK,"成功加入购物车");
    }

    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping(value = "/list")
    public Map list(){
        //静态获取
//        String username="changgou";
        //获取用户名
        String username=tokenDecode.getUserInfo().get("username");
        return cartService.list(username);
    }
}
