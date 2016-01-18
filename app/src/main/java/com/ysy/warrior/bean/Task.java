package com.ysy.warrior.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 任务的实体类【 2014-11-23 19:59:34】
 *
 * @author zyh
 */
public class Task extends BmobObject {

    private int id;
    private User user;
    /**
     * 名字
     */
    private String name;
    /**
     * 任务的时间
     */
    private long time;
    /**
     * 任务的标签
     */
    private int tagId;
    /**
     * 0.单次 1.每天 2.每周 3.每月
     */
    private int repeat;
    /**
     * 任务的笔记
     */
    private String note;
    /**
     * 卡片背景网络路径
     */
    private String imageUrl;
    /**
     * at的好友列表，存放好友
     */
    private List<String> atFriends;
    /**
     * 录音的网络路径
     */
    private String audioUrl;
    /**
     * 是否已经被提醒过
     */
    private boolean hasAlerted;
    /**
     * 是否提醒
     */
    private boolean needAlerted;
    /**
     * 判断是否按压，用于滑动
     */
    private boolean pinedToSwipeLeft;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getAtFriends() {
        return atFriends;
    }

    public void setAtFriends(List<String> atFriends) {
        this.atFriends = atFriends;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public boolean isHasAlerted() {
        return hasAlerted;
    }

    public void setHasAlerted(boolean hasAlerted) {
        this.hasAlerted = hasAlerted;
    }

    public boolean isNeedAlerted() {
        return needAlerted;
    }

    public void setNeedAlerted(boolean needAlerted) {
        this.needAlerted = needAlerted;
    }

    public boolean isPinedToSwipeLeft() {
        return pinedToSwipeLeft;
    }

    public void setPinedToSwipeLeft(boolean pinedToSwipeLeft) {
        this.pinedToSwipeLeft = pinedToSwipeLeft;
    }
}
