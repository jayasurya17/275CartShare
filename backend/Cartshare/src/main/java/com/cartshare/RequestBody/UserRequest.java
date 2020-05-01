package com.cartshare.RequestBody;

public class UserRequest {
    private String uid;
    private String email;
    private String nickName;
    private String screenName;
    private String isAdmin;
    private String isVerified;
    private String isActive;
    private String isProfileComplete;
    private String city;
    private String state;
    private String street; 
    private String zipcode;

    public String getUid() {
		return this.uid;
	}

    public String getEmail(){
        return this.email;
    }

    public String getNickName(){
        return this.nickName;
    }

    public String getScreenName(){
        return this.screenName;
    }

    public String getIsAdmin(){
        return this.isAdmin;
    }

    public String getIsVerified(){
        return this.isVerified;
    }

    public String getIsActive(){
        return this.isActive;
    }

    public String getIsProfileComplete(){
        return this.isProfileComplete;
    }

    public String getCity(){
        return this.city;
    }

    public String getState(){
        return this.state;
    }

    public String getStreet(){
        return this.street;
    }

    public String getZipcode(){
        return this.zipcode;
    }

    public void setUid(String uid) {
		this.uid = uid;
    }
    
    public void setEmail(String email){
        this.email = email;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public void setScreenName(String screenName){
        this.screenName = screenName;
    }

    public void setIsAdmin(String isAdmin){
        this.isAdmin = isAdmin;
    }

    public void setIsVerified(String isVerified){
        this.isVerified = isVerified;
    }

    public void setIsActive(String isActive){
        this.isActive = isActive;
    }

    public void setIsProfileComplete(String isProfileComplete){
        this.isProfileComplete = isProfileComplete;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setState(String state){
        this.state = state;
    }

    public void setStreet(String street){
        this.street = street;
    }

    public void setZipcode(String zipcode){
        this.zipcode = zipcode;
    }

}