package vn.iotstar.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", columnDefinition = "nvarchar(255) null")
    private String email;

    @Column(name = "username", columnDefinition = "varchar(100) not null", unique = true)
    private String userName;

    @Column(name = "fullname", columnDefinition = "nvarchar(255) null")
    private String fullName;

    @Column(name = "password", columnDefinition = "varchar(255) not null")
    private String passWord;

    @Column(name = "avatar", columnDefinition = "nvarchar(255) null")
    private String avatar;

    @Column(name = "roleid")
    private int roleid;

    @Column(name = "phone", columnDefinition = "varchar(20) null")
    private String phone;

    @Temporal(TemporalType.DATE)
    @Column(name = "createddate")
    private Date createdDate;

    @Column(name = "active", columnDefinition = "int not null default 1")
    private int active = 1;

    @Column(name = "activation_otp_hash", columnDefinition = "varchar(255) null")
    private String activationOtpHash;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activation_otp_expires_at")
    private Date activationOtpExpiresAt;

    @Column(name = "reset_otp_hash", columnDefinition = "varchar(255) null")
    private String resetOtpHash;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reset_otp_expires_at")
    private Date resetOtpExpiresAt;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getActivationOtpHash() {
        return activationOtpHash;
    }

    public void setActivationOtpHash(String activationOtpHash) {
        this.activationOtpHash = activationOtpHash;
    }

    public Date getActivationOtpExpiresAt() {
        return activationOtpExpiresAt;
    }

    public void setActivationOtpExpiresAt(Date activationOtpExpiresAt) {
        this.activationOtpExpiresAt = activationOtpExpiresAt;
    }

    public String getResetOtpHash() {
        return resetOtpHash;
    }

    public void setResetOtpHash(String resetOtpHash) {
        this.resetOtpHash = resetOtpHash;
    }

    public Date getResetOtpExpiresAt() {
        return resetOtpExpiresAt;
    }

    public void setResetOtpExpiresAt(Date resetOtpExpiresAt) {
        this.resetOtpExpiresAt = resetOtpExpiresAt;
    }
}
