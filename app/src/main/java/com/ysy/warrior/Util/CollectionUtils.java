package com.ysy.warrior.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.im.bean.BmobChatUser;

/**
 * User: ysy
 * Date: 2015/12/29
 * Time: 15:58
 */
public class CollectionUtils {

    public static boolean isNotNull(Collection<?> collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * list转map 以用户名为key
     *
     * @return Map<String,BmobChatUser>
     * @throws
     */
    public static Map<String, BmobChatUser> list2map(List<BmobChatUser> users) {
        Map<String, BmobChatUser> friends = new HashMap<String, BmobChatUser>();
        for (BmobChatUser user : users) {
            friends.put(user.getUsername(), user);
        }
        return friends;
    }

    /**
     * map转list
     *
     * @return List<BmobChatUser>
     * @throws
     * @Title: map2list
     */
    public static List<BmobChatUser> map2list(Map<String, BmobChatUser> maps) {
        List<BmobChatUser> users = new ArrayList<BmobChatUser>();

        Iterator<Map.Entry<String, BmobChatUser>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BmobChatUser> entry = iterator.next();
            users.add(entry.getValue());
        }
        return users;
    }

}
