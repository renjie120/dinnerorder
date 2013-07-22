package bean;

import java.io.Serializable;

public class Login implements Serializable {
	// 序列号
	private static final long serialVersionUID = 1L;
	private String groupName; 
	private String pass;
	private int sno;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	} 
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	} 
	
}
