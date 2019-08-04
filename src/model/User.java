package model;
import java.io.Serializable;

public class User implements Serializable {
	private int id;
	private String loginId;
	private String pass;
	private String mail;
	private String name;
	private int age;
	private String imgPath;

	public User(){}
	public User(String loginId, String pass) {
		this.loginId = loginId;
		this.pass = pass;
	}

	public User(int id, String loginId, String pass, String mail, String name, int age) {
		this.id = id;
		this.loginId = loginId;
		this.pass = pass;
		this.mail = mail;
		this.name = name;
		this.age = age;
	}
	public User(String loginId, String pass, String mail, String name, int age) {
		this.loginId = loginId;
		this.pass = pass;
		this.mail = mail;
		this.name = name;
		this.age = age;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}







}
