package com.hawk.nacos.demo.springboot.domian;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Hawk
 * #@Proxy(lazy = false) 关闭懒加载
 */
@Entity
@Data
@Table(name = "temp_user")
@Proxy(lazy = false)
public class TempUserDo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "openid")
    private String openid;

    @Column(name = "unionid")
    private String unionid;

    @CreationTimestamp
    @Column(name = "add_time")
    private Date addTime;

    @CreationTimestamp
    @Column(name = "mod_time")
    private Date modTime;

}
