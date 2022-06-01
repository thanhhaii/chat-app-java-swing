package entity;

import java.util.Date;

public class Account {

    private String id;
    private String username;
    private String password;
    private String avatar;
    private String fullname;
    private String gender;
    private Date birthday;
    private String email;
    public Account() {
    }

    public Account(String id, String username, String password, String avatar, String fullname, String gender, Date birthday, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.fullname = fullname;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
    }

    public Account(String avatar, String fullname) {
        this.avatar = avatar;
        this.fullname = fullname;
    }

    public Account(String avatar, String fullname, String username, String id){
        this.avatar = avatar;
        this.fullname = fullname;
        this.username = username;
    }

    public Account(String avatar, String fullname, String username) {
        this.avatar = avatar;
        this.fullname = fullname;
        this.username = username;
    }

    public Account(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", fullname='" + fullname + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                '}';
    }
}
