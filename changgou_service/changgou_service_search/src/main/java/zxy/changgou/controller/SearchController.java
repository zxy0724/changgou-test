package zxy.changgou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxy.changgou.service.ESManagerService;
import zxy.changgou.service.SearchService;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/sku_search")
public class SearchController {
    @Autowired
    private ESManagerService esManagerService;
    @Autowired
    private SearchService searchService;

    //对搜索入参带有特殊符号进行处理
    public void handlerSearchMap(Map<String, String> searchMap) {
        if (searchMap != null) {
            Set<Map.Entry<String, String>> entries = searchMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if (entry.getKey().startsWith("spec_")) {
                    searchMap.put(entry.getKey(), entry.getValue().replace("+", "%2B"));
                }
            }
        }
    }

    /**
     * 全文检索
     *
     * @return
     */
    @GetMapping
    public Map search(@RequestParam Map<String, String> paramMap) throws Exception {
        //特殊符号处理
        handlerSearchMap(paramMap);
        Map resultMap = searchService.search(paramMap);
        return resultMap;
    }
}
