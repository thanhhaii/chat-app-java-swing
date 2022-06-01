package entity;

import javax.swing.*;
import java.util.Date;

public class Message{

    private String usernameSend;
    private String usernameReceiver;
    private String type;
    private Date time;
    private String content;
    private String nameGroup;
    private String iconGroup;
    private String groupId;
    private String groupMaker;

    public Message() {
    }

    public Message(String usernameSend, String usernameReceiver, String type, Date time, String content, String nameGroup, String iconGroup, String groupId) {
        this.usernameSend = usernameSend;
        this.usernameReceiver = usernameReceiver;
        this.type = type;
        this.time = time;
        this.content = content;
        this.nameGroup = nameGroup;
        this.iconGroup = iconGroup;
        this.groupId = groupId;
    }

    public Message(String iconGroup, String nameGroup) {
        this.iconGroup = iconGroup;
        this.nameGroup = nameGroup;
    }

    public String getGroupMaker() {
        return groupMaker;
    }

    public void setGroupMaker(String groupMaker) {
        this.groupMaker = groupMaker;
    }

    public String getUsernameSend() {
        return usernameSend;
    }

    public void setUsernameSend(String usernameSend) {
        this.usernameSend = usernameSend;
    }


    public void setUsernameReceiver(String usernameReceiver) {
        this.usernameReceiver = usernameReceiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsernameReceiver() {
        return usernameReceiver;
    }

    public Date getTime() {
        return time;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public String getIconGroup() {
        return iconGroup;
    }

    public void setIconGroup(String iconGroup) {
        this.iconGroup = iconGroup;
    }

    @Override
    public String toString() {
        return "Message{" +
                "usernameSend='" + usernameSend + '\'' +
                ", usernameReceiver='" + usernameReceiver + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", nameGroup='" + nameGroup + '\'' +
                ", iconGroup='" + iconGroup + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupMaker='" + groupMaker + '\'' +
                '}';
    }
}
