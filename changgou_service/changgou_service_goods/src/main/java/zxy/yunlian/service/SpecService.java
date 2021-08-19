package zxy.yunlian.service;

import java.util.List;
import java.util.Map;

public interface SpecService {
    /**
     * 根据分类名称来查询规格参数
     * @param categoryName
     * @return
     */
    List<Map> findSpecListByCategoryName(String categoryName);
}
