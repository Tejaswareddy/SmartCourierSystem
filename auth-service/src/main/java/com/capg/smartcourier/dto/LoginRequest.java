package com.capg.smartcourier.dto;


public class LoginRequest {
    private String username;
    private String password;
    
    public LoginRequest(){
    	
    }
    
    LoginRequest(String username, String password){
    	this.username = username;
    	this.password = password;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void serPassword(String password) {
    	this.password = password;
    }
}