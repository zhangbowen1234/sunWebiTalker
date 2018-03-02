package net.sun.web.italker.push.service;

import net.sun.web.italker.push.bean.api.account.AccountRspModel;
import net.sun.web.italker.push.bean.api.account.RegisterModel;
import net.sun.web.italker.push.bean.api.base.ResponseModel;
import net.sun.web.italker.push.bean.db.User;
import net.sun.web.italker.push.factory.UserFactory;

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
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            // 已有账户
            return ResponseModel.buildHaveAccountError();
        }
        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            // 已有用户名
            return ResponseModel.buildHaveNameError();
        }

        // 开始注册逻辑
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getName());

        if (user != null) {
            // 返回当前的账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk();
        } else {
            //注册异常
            return ResponseModel.buildRegisterError();
        }
    }
}
