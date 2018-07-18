package net.sun.web.italker.push.factory;

import net.sun.web.italker.push.bean.db.Group;
import net.sun.web.italker.push.bean.db.GroupMember;
import net.sun.web.italker.push.bean.db.User;

import java.util.Set;

/**
 * 群数据库处理
 * Created by bowen on 2018/7/18.
 */
public class GroupFactory {
    public static Group findById(String groupId) {
        // TODO 查询一个群
        return null;
    }

    public static Group findById(User sender, String receiverId) {
        // TODO 查询一个群，同时该User必须为群成员，否则返回null
        return null;
    }

    public static Set<GroupMember> getMembers(Group group) {
        // TODO 查询一个群的成员
        return null;
    }
}
