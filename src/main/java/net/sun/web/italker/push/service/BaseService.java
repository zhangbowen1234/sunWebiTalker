package net.sun.web.italker.push.service;

import net.sun.web.italker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class BaseService {
    // 添加一个上下文注解，该注解会给securityContext赋值
    // 具体的值为我们的拦截器中所返回的SecurityContext
    // protected 子类必须继承父类才能调到
    @Context
    protected SecurityContext securityContext;


    /**
     * 从上下文中直接获取自己的信息
     *
     * @return User
     */
    protected User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
