package jp.co.internous.ecsite.model.form;

import java.io.Serializable;

public class UserForm implements Serializable{
	private static final long serialVersionUID =1L;
	
	private long id;
	private String userName;
	private String password;
	private String fullName;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id=id;
		
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName=userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName=fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password=password;
	}
	
	
	

}
