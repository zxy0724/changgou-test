package zxy.changgou.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zxy.changgou.dao.TaskHisMapper;
import zxy.changgou.dao.TaskMapper;
import zxy.changgou.pojo.Task;
import zxy.changgou.pojo.TaskHis;
import zxy.changgou.service.TaskService;

import java.util.Date;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskHisMapper taskHisMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    @Transactional
    public void delTask(Task task) {
        //记录删除时间
        task.setDeleteTime(new Date());
        Long taskId = task.getId();
        task.setId(null);
        //Bean拷贝
        TaskHis taskHis = new TaskHis();
        BeanUtils.copyProperties(task, taskHis);
        //记录历史任务数据
        task.setId(taskId);
        taskMapper.deleteByPrimaryKey(task);
        System.out.println("订单服务完成了添加历史任务并删除原有任务的操作");
    }
}
