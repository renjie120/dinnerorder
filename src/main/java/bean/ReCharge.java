package bean;

import java.io.Serializable;

/**
 * 充值记录.
 * @author lsq
 *
 */
public class ReCharge implements Serializable {
	// 序列号
	private static final long serialVersionUID = 1L;
	private int peopleSno; 
	private String time;
	private String money;
	private int sno;
	public int getPeopleSno() {
		return peopleSno;
	}
	public void setPeopleSno(int peopleSno) {
		this.peopleSno = peopleSno;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
}
