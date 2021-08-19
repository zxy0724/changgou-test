package zxy.yunlian.service.impl;

import zxy.yunlian.dao.SpecMapper;
import zxy.yunlian.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class SpecServiceImpl implements SpecService {
    @Autowired
    private SpecMapper specMapper;

    /**
     * 1.根据分类名称来查询规格参数
     * 2.将options的值拆分为数组
     * @param categoryName
     * @return
     */
    @Override
    public List<Map> findSpecListByCategoryName(String categoryName) {
        List<Map> specList = specMapper.findSpecListByCategoryName(categoryName);
        for (Map map: specList) {
            //将options的值拆分为数组
            String[] options = ((String) map.get("options")).split(",");
            map.put("options",options);
        }
        return specList;
    }
}
