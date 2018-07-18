package net.sun.web.italker.push.factory;

import com.google.common.base.Strings;
import net.sun.web.italker.push.bean.api.base.PushModel;
import net.sun.web.italker.push.bean.card.MessageCard;
import net.sun.web.italker.push.bean.db.*;
import net.sun.web.italker.push.utils.Hib;
import net.sun.web.italker.push.utils.PushDispatcher;
import net.sun.web.italker.push.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息存储与处理的工具类
 * Created by bowen on 2018/7/17.
 */
public class PushFactory {
    // 发送一条消息，并在当前的发送的发送历史记录中存储记录
    public static void pushNewMessage(User sender, Message message) {
        if (sender == null || message == null)
            return;
        // 消息卡片用于发送
        MessageCard card = new MessageCard(message);
        // 要发送的字符串
        String entity = TextUtil.toJson(card);

        // 发送者
        PushDispatcher dispatcher = new PushDispatcher();

        if (message.getGroup() == null && Strings.isNullOrEmpty(message.getGroupId())) {
            // 给朋友发送消息

            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null)
                return;

            // 历史记录表建立
            PushHistory history = new PushHistory();
            // 普通消息类型
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setReceiver(receiver);
            // 接收者当前的社保推送Id
            history.setReceiverPushId(receiver.getPushId());

            // 推送的真实Model
            PushModel pushModel = new PushModel();
            // 每一条历史纪录都是独立的，可以单独的发送
            pushModel.add(history.getEntityType(), history.getEntity());

            // 把需要发送的数据，丢给发送者进行发送
            dispatcher.add(receiver, pushModel);
            //保存到数据库
            Hib.queryOnly(session -> session.save(history));
        } else {

            Group group = message.getGroup();
            // 因为延迟加载情况可能为Null，需要通过Id查询
            if (group == null)
                group = GroupFactory.findById(message.getGroupId());

            // 如果群真的没有，
            if (group == null)
                return;
            // 给群成员发送消息
            Set<GroupMember> members = GroupFactory.getMembers(group);
            if (members == null || members.size() == 0)
                return;

            // 过滤我自己
            members = members.stream()
                    .filter(groupMember -> !groupMember.getUserId()
                            .equalsIgnoreCase(sender.getId()))
                    .collect(Collectors.toSet());

            // 一个历史记录表
            List<PushHistory> histories = new ArrayList<>();
            addGroupMembersPushModel(dispatcher, // 推送的发送者
                    histories, // 数据库要存储的列表
                    members, // 所以的成员
                    entity, // 要发送的数据
                    PushModel.ENTITY_TYPE_MESSAGE); // 发送的类型

            Hib.queryOnly(session -> {
                // 保存数据库的操作
                for (PushHistory history : histories) {
                    session.saveOrUpdate(history);
                }
            });
        }

        // 发送者进行真实的提交
        dispatcher.submit();
    }

    /**
     * 给群成员构建一个消息，
     * 把消息存储到数据库的历史纪录中，每个人，每条消息都是一个记录
     */
    private static void addGroupMembersPushModel(PushDispatcher dispatcher,
                                                 List<PushHistory> histories,
                                                 Set<GroupMember> members,
                                                 String entity,
                                                 int entityTypeMessage) {
        for (GroupMember member : members) {
            // 即加载，无需通过Id再去找用户
            User receiver = member.getUser();
            if (receiver == null)
                return;

            // 历史记录表建立
            PushHistory history = new PushHistory();
            history.setEntityType(entityTypeMessage);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId());
            histories.add(history);

            // 构建一个消息Model
            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());

            // 添加到发送者的数据集中
            dispatcher.add(receiver, pushModel);
        }
    }
}
