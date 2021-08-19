package zxy.changgou.service.impl;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zxy.changgou.pojo.SkuInfo;
import zxy.changgou.service.SearchService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    //设置每页查询条数据
    public final static Integer PAGE_SIZE = 20;

    /**
     * 全文检索
     *
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public Map search(Map<String, String> paramMap) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //有条件才查询ES\
        if (paramMap != null) {
            //keywords的参数值不为空
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //关键字
            if (!StringUtils.isEmpty(paramMap.get("keywords"))) {
                //name:表示按照name域进行查询
                //matchQuery
                boolQueryBuilder.must(QueryBuilders.matchQuery("name", paramMap.get("keywords")).operator(Operator.AND));
            }
            //品牌条件
            if (!StringUtils.isEmpty(paramMap.get("brand"))) {
                //name:表示按照name域进行查询
                //matchQuery
                boolQueryBuilder.must(QueryBuilders.matchQuery("brandName", paramMap.get("brand")).operator(Operator.AND));

            }
            //条件 规格
            for (String key : paramMap.keySet()) {
                if (key.startsWith("spec_")) {
                    String value = paramMap.get(key).replace("%2B", "+");
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }
            //条件 价格
            if (StringUtils.isEmpty(paramMap.get("price"))) {
                String[] p = paramMap.get("price").split("-");
                if (p.length == 2) {
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(p[1]));
                }
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(p[0]));
            }
            //原生搜索实现类
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
//            //品牌分组查询
//            String skuBrand = "skuBrand";
//            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuBrand).field("brandName"));
//
//            //规格分组查询
//            String skuSpec = "skuSpec";
//            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuSpec).field("spec.keyword"));

            //执行查询，返回结果对象
            /**
             *  第一个参数:条件构建对象
             *  第二个参数:查询实体类
             *  第三个参数:查询结果操作对象
             */
            AggregatedPage<SkuInfo> aggregatedPage = esTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    List<T> list = new ArrayList<>();
                    SearchHits hits = searchResponse.getHits();
                    if (hits != null) {
                        for (SearchHit hit : hits) {
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            list.add((T) skuInfo);
                        }

                    }
                    return new AggregatedPageImpl<T>(list, pageable, hits.getTotalHits(), searchResponse.getAggregations());
                }
            });
            //总条数
            resultMap.put("total", aggregatedPage.getTotalElements());
            //总页数
            resultMap.put("totalPages", aggregatedPage.getTotalPages());
            //查询结果合集
            resultMap.put("rows", aggregatedPage.getContent());
//            //获取品牌分组结果
//            StringTerms brandTerms= (StringTerms)aggregatedPage.getAggregation(skuBrand);
//            List<String> brandList = brandTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
//            resultMap.put("brandList",brandList);
//
//            //获取规格分组结果
//            StringTerms specTerms= (StringTerms)aggregatedPage.getAggregation(skuSpec);
//            List<String> specList = brandTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
//            resultMap.put("specList",specList);

            return resultMap;
        }

        return null;
    }

    //处理规格合集 源码中的方法为formartSpec
    public Map<String, Set<String>> specList(List<String> specList) {
        Map<String, Set<String>> specMap = new HashMap<>();
        if (specList != null && specList.size() > 0) {
            for (String spec : specList) {
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Set<String> specValues = specMap.get(key);
                    if (specValues == null) {
                        specValues = new HashSet<>();
                    }
                    specValues.add(value);
                    specMap.put(key, specValues);
                }
            }
        }
        return null;
    }
}
