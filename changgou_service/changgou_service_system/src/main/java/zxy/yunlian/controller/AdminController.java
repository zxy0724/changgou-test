package zxy.yunlian.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zxy.yunlian.JwtUtil;
import zxy.yunlian.pojo.Admin;
import zxy.yunlian.service.AdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;


    @GetMapping
    public Result findAll() {
        List<Admin> brandList = adminService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", brandList);
    }

    @PostMapping
    public Result add(@RequestBody Admin admin) {
        if (admin == null) {
            return new Result(true, StatusCode.ERROR, "注册失败");
        }
        adminService.add(admin);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        boolean login = adminService.login(admin);
        if (login) {
            Map<String, String> info = new HashMap<>();
            info.put("username", admin.getLoginName());
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), admin.getLoginName(), null);
            info.put("token", token);
            return new Result(true, StatusCode.OK, "登录成功", info);
        } else {
            return new Result(false, StatusCode.LOGINERROR, "用户名或密码错误");
        }
    }
}
