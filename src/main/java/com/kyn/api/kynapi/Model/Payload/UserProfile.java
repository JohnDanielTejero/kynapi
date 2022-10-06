package com.kyn.api.kynapi.Model.Payload;

import java.util.Date;
import java.util.Set;

import com.kyn.api.kynapi.Model.Roles;
import com.kyn.api.kynapi.Model.User;

public class UserProfile {
    
    private String email;

    private String firstName;

    private String lastName;

    private String contactNo;

    private String imgPath;

    private Date birthDate;

    private Set<Roles> Role;

    public UserProfile(User user){
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.contactNo = user.getContactNo();
        this.imgPath = user.getImgPath();
        this.birthDate = user.getBirthDate(); 
        this.Role = user.getRole();
    }

    public UserProfile(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Roles> getRole() {
        return Role;
    }

    public void setRole(Set<Roles> role) {
        Role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
