package vn.iotstar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User_24162046 implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Nationalized
    @Column(name = "fullname", length = 50)
    private String fullname;

    @Column(name = "phone")
    private Integer phone;

    // MD5 hex = 32 ky tu
    @Column(name = "passwd", length = 32, nullable = false)
    private String passwd;

    @Column(name = "signup_date", columnDefinition = "datetime")
    private LocalDateTime signupDate;

    @Column(name = "last_login", columnDefinition = "datetime")
    private LocalDateTime lastLogin;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    public User_24162046() {
    }

    /** Dung trong JSP: ${account.adminRole} */
    public boolean isAdminRole() {
        return Boolean.TRUE.equals(isAdmin);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
