package net.sun.web.italker.push.service;
import net.sun.web.italker.push.bean.api.account.RegisterModel;
import net.sun.web.italker.push.bean.db.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author qiujuer
 */
// 127.0.0.1/api/account/...
@Path("/account")
public class AccountService {

    @POST
    @Path("/register")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RegisterModel register(RegisterModel model) {
        return model;
//        User user = new User();
//        user.setName("美女");
//        user.setSex(2);
//        return user;
    }
}
