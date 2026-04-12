package com.wisdom.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_role")
public class Role implements Serializable {
    private Long id;
    private String roleName;
    private String roleKey;
    private String permissions;
    private LocalDateTime createTime;
}