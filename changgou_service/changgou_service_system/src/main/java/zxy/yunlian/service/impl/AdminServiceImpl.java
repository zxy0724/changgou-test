package zxy.yunlian.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import zxy.yunlian.dao.AdminMapper;
import zxy.yunlian.pojo.Admin;
import zxy.yunlian.service.AdminService;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void add(Admin admin) {
        admin.setStatus("1");
        admin.setPassword(BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt()));
        adminMapper.insert(admin);
    }

    @Override
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    @Override
    public boolean login(Admin admin) {
        Admin admin1 = new Admin();
        admin1.setStatus("1");
        admin1.setLoginName(admin.getLoginName());
        admin1 = adminMapper.selectOne(admin1);
        return BCrypt.checkpw(admin.getPassword(), admin1.getPassword());
    }
}
