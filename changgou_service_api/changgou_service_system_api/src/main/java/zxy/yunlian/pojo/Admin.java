package zxy.yunlian.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Table(name = "tb_admin")
@Data
public class Admin implements Serializable {
    @Id
    private Integer id;
    private String loginName;
    private String password;
    private String status;

}
