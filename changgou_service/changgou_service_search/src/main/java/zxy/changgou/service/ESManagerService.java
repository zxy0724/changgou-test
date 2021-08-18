package zxy.changgou.service;

public interface ESManagerService {

    /**
     * 创建索引库结构
     */
    void createMappingAndIndex();

    /**
     * 导入全部数据到ES索引库
     */
    void importAll();

    /**
     * 根据spuid导入数据到ES索引库
     *
     * @param spuId 商品id
     */
    void importDataBySpuId(String spuId);


    //根据souid删除es索引库中相关的sku数据
    void delDataBySpuId(String spuId);
}