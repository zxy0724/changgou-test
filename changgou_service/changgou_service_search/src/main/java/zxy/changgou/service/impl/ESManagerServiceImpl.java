package zxy.changgou.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import zxy.changgou.dao.ESManagerMapper;
import zxy.changgou.pojo.SkuInfo;
import zxy.changgou.service.ESManagerService;
import zxy.yunlian.feign.SkuFeign;
import zxy.yunlian.pojo.Sku;

import java.util.List;
import java.util.Map;

@Service
public class ESManagerServiceImpl implements ESManagerService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ESManagerMapper esManagerMapper;

    /**
     * 创建索引库结构
     */
    @Override
    public void createMappingAndIndex() {
        //创建索引
        elasticsearchTemplate.createIndex(SkuInfo.class);
        //创建映射
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    /**
     * 导入全部数据到ES索引库
     */
    @Override
    public void importAll() {
        List<Sku> skuList = skuFeign.findSkuListBySpuId("all");
        if (skuList == null || skuList.size() <= 0) {
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        //skulist转换为json
        String jsonSkuList = JSON.toJSONString(skuList);
        //将json转换为skuinfo
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }
        esManagerMapper.saveAll(skuInfoList);
    }

    /**
     * 根据spuid导入数据到ES索引库
     *
     * @param spuId 商品id
     */
    @Override
    public void importDataBySpuId(String spuId) {
        List<Sku> skuList = skuFeign.findSkuListBySpuId(spuId);
        if (skuList == null || skuList.size() <= 0) {
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        //将集合转换为json
        String jsonSkuList = JSON.toJSONString(skuList);
        //将json转换为skuinfo
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            //将规格信息进行转换
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }
        //添加索引库
        esManagerMapper.saveAll(skuInfoList);
    }

    @Override
    public void delDataBySpuId(String spuId) {
        List<Sku> skuList = skuFeign.findSkuListBySpuId(spuId);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        for (Sku sku : skuList) {
            esManagerMapper.deleteById(Long.parseLong(sku.getId()));
        }
    }
}