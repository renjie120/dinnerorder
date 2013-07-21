package bean;

import java.io.Serializable;

/**
 * 人员信息.
 * @author lsq
 *
 */
public class People implements Serializable {
	// 序列号
	private static final long serialVersionUID = 1L;
	private String name;
	private int sno;
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	} 
	 
}
