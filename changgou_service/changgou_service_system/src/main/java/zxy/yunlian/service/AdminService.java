package zxy.yunlian.service;

import zxy.yunlian.pojo.Admin;

import java.util.List;

public interface AdminService {
    public void add(Admin admin);
    public List<Admin> findAll();
    public boolean login(Admin admin);
}
