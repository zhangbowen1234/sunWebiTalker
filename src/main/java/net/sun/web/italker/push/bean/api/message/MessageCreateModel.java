package net.sun.web.italker.push.bean.api.message;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import net.sun.web.italker.push.bean.api.user.UpdateInfoModel;
import net.sun.web.italker.push.bean.db.Message;

/**
 * API请求的Model格式
 * Created by bowen on 2018/7/17.
 */
public class MessageCreateModel {
    // ID从客户端生成一个UUID
    @Expose
    private String id;

    // 内容不允许为空，类型为text
    @Expose
    private String content;

    // 附件
    @Expose
    private String attach;

    // 消息类型
    @Expose
    private int type = Message.TYPE_STR;

    // 发送者 不为空
    @Expose
    private String senderId;

    // 接收者 可为空
    @Expose
    private String receiverId;

    // 接收者类型，群，人
    @Expose
    private int receiverType = Message.RECEUVER_TYPE_NONE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    // 检查操作
    public static boolean check(MessageCreateModel model) {
        return model != null
                && !(Strings.isNullOrEmpty(model.id)
                || Strings.isNullOrEmpty(model.senderId)
                || Strings.isNullOrEmpty(model.content)
                || Strings.isNullOrEmpty(model.receiverId))

                && (model.receiverType == Message.RECEUVER_TYPE_NONE
                || model.receiverType == Message.RECEUVER_TYPE_GROUP)

                && (model.type == Message.TYPE_AUDIO
                || model.type == Message.TYPE_PIC
                || model.type == Message.TYPE_FILE
                || model.type == Message.TYPE_STR);
    }

}
