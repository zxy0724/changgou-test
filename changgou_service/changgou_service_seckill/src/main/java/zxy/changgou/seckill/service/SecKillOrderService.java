package zxy.changgou.seckill.service;

public interface SecKillOrderService {
    /**
     * 秒杀下单
     * @param id 商品id
     * @param time 时间段
     * @param username 登录人姓名
     * @return
     */
    boolean add(Long id,String time,String username);
}
