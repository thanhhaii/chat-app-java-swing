package dao;

import common.DBDataAccessHelper;
import entity.Account;
import org.mindrot.jbcrypt.BCrypt;
import view.Login;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountDao {

    public int registerAccount(String username, String password, String fullname, String gender, Date birthday, String email){
        var checkAddAcc = 0;
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{? = call createNewAccount(?,?,?,?,?,?,?)}")
        ){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            cs.setString(3, hashPass(password));
            cs.setString(4, gender.equals("Male")?"image/male.png":"image/female.png");
            cs.setString(5, fullname);
            cs.setString(6, gender);
            cs.setDate(7, birthday);
            cs.setString(8, email);
            cs.execute();
            checkAddAcc = cs.getInt(1);
        }catch (Exception e1){
            e1.printStackTrace();
        }
        return checkAddAcc;
    }

    public void changePassword(String oldPassword, String newPassword){
        var sql = new String[]{"select password from account where username = ?","update account set [password] = ? where username = ?"};
        try(
                var cs1 = DBDataAccessHelper.connection().prepareStatement(sql[0]);
                var cs2 = DBDataAccessHelper.connection().prepareStatement(sql[1]);
            ){
            cs1.setString(1, Login.usernameUser);
            var rs = cs1.executeQuery();
            while (rs.next()){
                if(BCrypt.checkpw(oldPassword, rs.getString("password"))){
                    cs2.setString(1, hashPass(newPassword));
                    cs2.setString(2, Login.usernameUser);
                    cs2.execute();
                    JOptionPane.showMessageDialog(null, "Password changed successfully");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resetPassword(String username, String newPassword){
        var sql = "update account set [password] = ? where username = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
        ){
            cs.setString(1, hashPass(newPassword));
            cs.setString(2, username);
            cs.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean passwordComparison(String password){
        var checkPass = false;
        var sql = "select password from account where username = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
            cs.setString(1, Login.usernameUser);
            var rs = cs.executeQuery();
            while (rs.next()){
                checkPass = BCrypt.checkpw(password, rs.getString("password"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return checkPass;
    }

    public boolean login(String username, String password){
        var check = false;
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{? = call loginAccount(?)}");
                var cs1 = DBDataAccessHelper.connection().prepareStatement("select password from account where username = ?");
            ){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            cs.execute();
            if(cs.getInt(1) == 1){
                cs1.setString(1,username);
                var rs = cs1.executeQuery();
                while (rs.next()) {
                    if (BCrypt.checkpw(password, rs.getString(1))) {
                        check = true;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }

    private String hashPass(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public void forgotPassword(String username){
        var checkExists = 0;
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{? = call checkAccountExists(?)}")
            ){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            cs.execute();
            checkExists = cs.getInt(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(checkExists == 1){
            final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                int randomIndex = random.nextInt(chars.length());
                sb.append(chars.charAt(randomIndex));
            }
            sendMail(getInfoUser(username).get(0).getEmail(), "Reset Password Account",sb + " is your new password.\nPlease change the password when logging into the app.");
            resetPassword(username, sb.toString());
            JOptionPane.showMessageDialog(null,"A new password has been sent to your email. Please check your email");
        }else {
            JOptionPane.showMessageDialog(null,"Username does not exist. Please check your username");
        }
    }

    public static void sendMail(String to,String title, String content) {
        // Recipient's email ID needs to be mentioned.

        // Sender's email ID needs to be mentioned
        String from ="hai.lnt34@aptechlearning.edu.vn";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";
        String password ="0965140743hai";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl",true);
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hai.lnt34@aptechlearning.edu.vn", "0965140743hai");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(title);

            // Now set the actual message
            message.setText(content);

            System.out.println("sending...");
            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    public List<Account> getInfoUser(String username){
        var list = new ArrayList<Account>();
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call getInfoUser(?)}");
            ){
            cs.setString(1, username);
            var rs = cs.executeQuery();
            while (rs.next()){
                var account = new Account();
                account.setUsername(rs.getString("username"));
                account.setFullname(rs.getString("fullname"));
                account.setGender(rs.getString("gender"));
                account.setBirthday(rs.getDate("birthday"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getString("avatar"));
                list.add(account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Account> findFriends(String idOrName){
        var list = new ArrayList<Account>();
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call searchUser(?)}")
            ){
            cs.setString(1, idOrName);
            var rs = cs.executeQuery();
            while (rs.next()){
                var user = new Account();
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setAvatar(rs.getString("avatar"));
                list.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Account> loadFriend(){
        var list = new ArrayList<Account>();
        String[] sql = new String[]{"select  * from account  JOIN friend on account.username = friend.userSendRequest where userReceiverRequest = ? and [status] = 'accepted'", "select * from account JOIN friend on account.username = friend.userReceiverRequest where userSendRequest = ? and [status] = 'accepted'"};
        try(
                var cs1 = DBDataAccessHelper.connection().prepareStatement(sql[0]);
                var cs2 = DBDataAccessHelper.connection().prepareStatement(sql[1]);
            ){
            cs1.setString(1, Login.usernameUser);
            cs2.setString(1, Login.usernameUser);
            resultQueryWithFriend(list, cs1, cs2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private void resultQueryWithFriend(ArrayList<Account> list, PreparedStatement cs1, PreparedStatement cs2) throws SQLException {
        var rs1 = cs1.executeQuery();
        var rs2 = cs2.executeQuery();
        while (rs1.next()){
            var account = new Account();
            account.setFullname(rs1.getString("fullname"));
            account.setAvatar(rs1.getString("avatar"));
            account.setUsername(rs1.getString("username"));
            list.add(account);
        }
        while (rs2.next()){
            var account = new Account();
            account.setFullname(rs2.getString("fullname"));
            account.setAvatar(rs2.getString("avatar"));
            account.setUsername(rs2.getString("username"));
            list.add(account);
        }
    }

    public void updateInfo(String fullname, String gender, Date birthday, String email){
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call updateInfo(?,?,?,?,?)}");
            ){
            cs.setString(1,fullname);
            cs.setString(2,gender);
            cs.setDate(3,birthday);
            cs.setString(4, email);
            cs.setString(5, Login.usernameUser);
            cs.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateAvatar(String path){
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement("update account set avatar = ? where username = ?");
            ){
            cs.setString(1,path);
            cs.setString(2,Login.usernameUser);
            cs.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int actionWithFriend(String usernameMyFriend, String action){
        var getProfileMyFriend = 0;
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{? = call actionWithFriend(?,?,?)}")
            ){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, usernameMyFriend);
            cs.setString(3, Login.usernameUser);
            cs.setString(4, action);
            cs.execute();
            getProfileMyFriend = cs.getInt(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getProfileMyFriend;
    }

    public int checkStatusFriend(String usernameFriend){
        var checkStatus = 0;
        try (
                var cs = DBDataAccessHelper.connection().prepareCall("{? = call checkStatus(?,?)}")
            ){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, usernameFriend);
            cs.setString(3, Login.usernameUser);
            cs.execute();
            checkStatus = cs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkStatus;
    }

    public List<Account> getRequestAccount(String request){
        var list = new ArrayList<Account>();
        try(
                var cs = DBDataAccessHelper.connection().prepareCall("{call getRequestAccount(?,?)}");
        ){
            cs.setString(1, Login.usernameUser);
            cs.setString(2, request);
            var rs = cs.executeQuery();
            while (rs.next()){
                var account = new Account();
                account.setUsername(rs.getString("username"));
                account.setFullname(rs.getString("fullname"));
                account.setGender(rs.getString("gender"));
                account.setBirthday(rs.getDate("birthday"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getString("avatar"));
                list.add(account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public void online(boolean onOff){
        var sql = "update account set online = ? where username = ?";
        try (
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
            cs.setBoolean(1, onOff);
            cs.setString(2, Login.usernameUser);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkUserOnline(String username){
        var online = false;
        var sql = "select online from account where username = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
            cs.setString(1, username);
            var rs = cs.executeQuery();
            while (rs.next()) {
                online = rs.getBoolean("online");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return online;
    }

    public List<Account> searchFriend(String usernameSearch){
        var list = new ArrayList<Account>();
        var sql = new String[]{"select  * from account  JOIN friend on account.username = friend.userReceiverRequest where userSendRequest = ? and [status] = 'accepted' and userReceiverRequest like '%'+?+'%'", "select  * from account  JOIN friend on account.username = friend.userSendRequest where userReceiverRequest = ? and [status] = 'accepted' and userSendRequest like '%'+?+'%'"};
        try (
                var cs1 = DBDataAccessHelper.connection().prepareStatement(sql[0]);
                var cs2 = DBDataAccessHelper.connection().prepareStatement(sql[1]);
            ){
            cs1.setString(1,Login.usernameUser);
            cs1.setString(2,usernameSearch);
            cs2.setString(1,Login.usernameUser);
            cs2.setString(2,usernameSearch);
            resultQueryWithFriend(list, cs1, cs2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<Account> getMemberGroupChat(int roomId){
        var list = new ArrayList<Account>();
        var sql = "select username, fullname, avatar from user_room join account on user_room.usernameMember = account.username where roomId = ? order by username";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
        ){
            cs.setInt(1, roomId);
            var rs = cs.executeQuery();
            while (rs.next()){
                var account = new Account();
                account.setFullname(rs.getString("fullname"));
                account.setUsername(rs.getString("username"));
                account.setAvatar(rs.getString("avatar"));
                list.add(account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public boolean checkFriendGroup(String roomId){
        var checkFriend = false;
        var sql = "select count(*) from user_room where usernameMember = ? and roomId =?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
//            cs.
        }catch (Exception e){
            e.printStackTrace();
        }
        return checkFriend;
    }


    public void insertMemberGroup(int roomId, String usernameMember){
        var sql = "insert into user_room(roomId, usernameMember, adminstrators) values (?,?,0) ";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
            ){
            cs.setInt(1, roomId);
            cs.setString(2, usernameMember);
            cs.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteMemberGroup(int roomId, String usernameMember){
        var sql = "delete from user_room where roomId = ? and usernameMember = ?";
        try(
                var cs = DBDataAccessHelper.connection().prepareStatement(sql);
        ){
            cs.setInt(1, roomId);
            cs.setString(2, usernameMember);
            cs.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void userOnline(String username, JLabel status){
        status.setVisible(true);
        if (checkUserOnline(username)) {
            status.setIcon(new ImageIcon(getClass().getResource("/image/online.png")));
            status.setText("Online");
            status.setForeground(new Color(0, 183, 26));
        } else {
            status.setIcon(new ImageIcon(getClass().getResource("/image/offline.png")));
            status.setText("Offline");
            status.setForeground(new Color(143, 143, 143));
        }
    }

}


