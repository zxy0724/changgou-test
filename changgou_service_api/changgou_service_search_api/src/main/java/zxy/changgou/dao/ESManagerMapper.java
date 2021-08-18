package zxy.changgou.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import zxy.changgou.pojo.SkuInfo;

public interface ESManagerMapper extends ElasticsearchRepository<SkuInfo, Long> {
}
