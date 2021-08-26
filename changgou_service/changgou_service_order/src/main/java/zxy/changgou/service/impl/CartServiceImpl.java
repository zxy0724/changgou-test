package zxy.changgou.service.impl;

import com.changgou.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zxy.changgou.pojo.OrderItem;
import zxy.changgou.service.CartService;
import zxy.yunlian.feign.SkuFeign;
import zxy.yunlian.feign.SpuFeign;
import zxy.yunlian.pojo.Sku;
import zxy.yunlian.pojo.Spu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private static final String CART = "Cart_";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;

    /**
     * 添加购物车
     *
     * @param skuId
     * @param num
     * @param username
     */
    @Override
    public void add(String skuId, Integer num, String username) {
        //查询Redis的数据
        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(CART + username).get(skuId);
        //判断是否存在
        if (orderItem != null) {
            //存在则刷新购物车
            orderItem.setNum(orderItem.getNum() + num);
            if (orderItem.getNum() <= 0) {
                redisTemplate.boundHashOps(CART+username).delete(skuId);
                return;
            }
            orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum() * orderItem.getPrice());
        } else {
            //不存在则添加购物车
            Result<Sku> skuResult = skuFeign.findById(skuId);
            Sku sku = skuResult.getData();
            Spu spu = spuFeign.findSpuById(sku.getSpuId()).getData();
            //将SKU转换成OrderItem
            orderItem = this.sku2OrderItem(sku, spu, num);
        }

        //存入redis
        redisTemplate.boundHashOps(CART + username).put(skuId, orderItem);
    }

    /**
     * 获取购物车列表数据
     * @param username
     * @return
     */
    @Override
    public Map list(String username) {
        Map map = new HashMap();
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(CART + username).values();
        map.put("orderItemList",orderItemList);

        //商品数量与总价格
        Integer totalNum=0;
        Integer totalPrice=0;
        for (OrderItem orderItem : orderItemList) {
            totalNum+=orderItem.getNum();
            totalPrice+=orderItem.getPrice();
        }
        map.put("totalNum",totalNum);
        map.put("totalPrice",totalPrice);

        return map;
    }

    //sku转换为orderItem
    private OrderItem sku2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);

        orderItem.setMoney(num * orderItem.getPrice());       //单价*数量
        orderItem.setPayMoney(num * orderItem.getPrice());    //实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight() * num);           //重量=单个重量*数量

        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
