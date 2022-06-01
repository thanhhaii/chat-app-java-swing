package dao;

import common.DBDataAccessHelper;
import entity.Account;
import entity.Message;
import view.Login;
import view.Main;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MessageDao {

    public List<Message> loadMessage(String usernameFriend){
        var list = new ArrayList<Message>();
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call loadMessage(?,?)}");
        ){
            cs.setString(1, Login.usernameUser);
            cs.setString(2,usernameFriend);
            var rs = cs.executeQuery();
            while (rs.next()){
                var message = new Message();
                message.setUsernameSend(rs.getString("usernameSend"));
                message.setUsernameReceiver(rs.getString("usernameReceiver"));
                message.setType(rs.getString("typeContent"));
                message.setContent(rs.getString("content"));
                list.add(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> loadMessageGroup(int roomId){
        var list = new ArrayList<Message>();
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call loadMessageGroup(?,?)}");
        ){
            cs.setString(1, Login.usernameUser);
            cs.setInt(2,roomId);
            var rs = cs.executeQuery();
            while (rs.next()){
                var message = new Message();
                message.setUsernameSend(rs.getString("usernameSend"));
                message.setUsernameReceiver(rs.getString("roomReceiver"));
                message.setType(rs.getString("typeContent"));
                message.setContent(rs.getString("content"));
                list.add(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public void insertMessage(String usernameFriend, String typeContent, String content){
        try (
                var cs = DBDataAccessHelper.connection().prepareCall("{call insertMessage(?,?,?,?)}");
        ){
            cs.setString(1,Login.usernameUser);
            cs.setString(2, usernameFriend);
            cs.setString(3, typeContent);
            cs.setString(4, content);
            cs.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Message getLastMessageSend(String usernameFriend){
        var message = new Message();
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call getLastMessageSend(?,?)}")
            ){
            cs.setString(1, Login.usernameUser);
            cs.setString(2, usernameFriend);
            var rs = cs.executeQuery();
            while (rs.next()){
                if(Login.usernameUser.equals(rs.getString("usernameSend"))){
                    if(rs.getString("typeContent").equals("text")) {
                        message.setContent("You: " + rs.getString("content"));
                    }else if(rs.getString("typeContent").equals("emoji")){
                        message.setContent("You: " + new ImageIcon(rs.getString("content")).getImage());
                    }
                }else {
                    if(rs.getString("typeContent").equals("text")) {
                        message.setContent(rs.getString("content"));
                    }else if(rs.getString("typeContent").equals("emoji")){
                        message.setContent("" + new ImageIcon(rs.getString("content")).getImage());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return message;
    }


    public void sendMessage(DataOutputStream dos, String usernameFriend, String message, String friendOrGroup){
        if(friendOrGroup.equals("friend")) {
            try {
                dos.writeUTF("Text");
                dos.writeUTF(usernameFriend);
                dos.writeUTF(message);
                dos.flush();
                insertMessage(usernameFriend, "text", message);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else if(friendOrGroup.equals("group")){
            try{
                dos.writeUTF("TextGroup");
                dos.writeUTF(usernameFriend);
                dos.writeUTF(message);
                dos.flush();
                insertMessageGroup(usernameFriend, "text", message);
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
        }
    }

    public Style printNameSend(boolean yourMessage, StyledDocument doc, String username){
        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }
        if (yourMessage) {
            StyleConstants.setForeground(userStyle, Color.RED);
        } else {
            StyleConstants.setForeground(userStyle, Color.BLUE);
        }
        try {
            doc.insertString(doc.getLength(), (username.equals(Login.usernameUser) ? "Me" : username) + ": ", userStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return userStyle;
    }

    public void seenMessage(String usernameFriend, boolean seen){
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call seenMessage(?,?,?)}");
            ){
            cs.setString(1, Login.usernameUser);
            cs.setString(2, usernameFriend);
            cs.setBoolean(3, seen);
            cs.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void newGroupChat(String nameGroup){
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call createGroupChat(?,?)}");
            ){
            cs.setString(1, nameGroup);
            cs.setString(2, Login.usernameUser);
            cs.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Message> getGroupChat(){
        var list = new ArrayList<Message>();
        var sql = "select * from user_room join chat_rooms on user_room.roomId = chat_rooms.roomId where usernameMember = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
            cs.setString(1, Login.usernameUser);
            var rs = cs.executeQuery();
            while (rs.next()){
                var message = new Message();
                message.setNameGroup(rs.getString("RoomName"));
                message.setIconGroup(rs.getString("avatar"));
                message.setGroupId(rs.getString("roomId"));
                message.setGroupMaker(rs.getString("usernameMaker"));
                list.add(message);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> defaultGroupChat(int roomId){
        var list = new ArrayList<Message>();
        var sql = "select * from chat_rooms where roomId = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
        ){
            cs.setInt(1, roomId);
            var rs = cs.executeQuery();
            while (rs.next()){
                var message = new Message();
                message.setNameGroup(rs.getString("RoomName"));
                message.setIconGroup(rs.getString("avatar"));
                message.setGroupId(rs.getString("roomId"));
                list.add(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public void insertMessageGroup(String roomId, String typeContent, String content){
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call insertMessageRoom(?,?,?,?)}");
            ){
            cs.setString(1, Login.usernameUser);
            cs.setInt(2, Integer.parseInt(roomId));
            cs.setString(3, typeContent);
            cs.setString(4, content);
            cs.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkAdminstrator(int roomId){
        var checkAdmin = false;
        var sql = "select adminstrators from user_room where roomId = ? and usernameMember = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
            cs.setInt(1, roomId);
            cs.setString(2, Login.usernameUser);
            var rs = cs.executeQuery();
            while (rs.next()){
                checkAdmin = rs.getBoolean("adminstrators");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return checkAdmin;
    }

    public void updateGroup(String avatar, String RoomName){
        var sql = "update chat_rooms set avatar = ?, RoomName = ? where roomId = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
            cs.setString(1, avatar);
            cs.setString(2, RoomName);
            cs.setInt(3, Main.roomId);
            cs.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
