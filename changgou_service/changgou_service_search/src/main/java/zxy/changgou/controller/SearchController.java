package zxy.changgou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @ResponseBody
    public Map search(@RequestParam Map<String, String> paramMap) throws Exception {
        //特殊符号处理
        handlerSearchMap(paramMap);
        Map resultMap = searchService.search(paramMap);
        return resultMap;
    }

    //搜索页面   http://localhost:9009/search/list?keywords=手机&brand=三星&spec_颜色=粉色&
    //入参：Map
    //返回值 Map
    //由于页面是thymeleaf 完成的 属于服务器内页面渲染 跳转页面
    @GetMapping("/list")
    public String search(@RequestParam Map<String, String> searchMap, Model model) throws Exception {

        //特殊符号处理
        handlerSearchMap(searchMap);

        //执行查询返回值
        Map<String, Object> resultMap = searchService.search(searchMap);

        model.addAttribute("searchMap", searchMap);
        model.addAttribute("result", resultMap);
        return "search";
    }
}
