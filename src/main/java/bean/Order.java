package bean;

import java.io.Serializable;

public class Order implements Serializable {
	// 序列号
	private static final long serialVersionUID = 1L;
	private String time;
	private String money;
	private int peopleSno;
	private int sno;
	private String dinnerName;
	private int dinnerNameList;
	public String getDinnerName() {
		return dinnerName;
	}

	public void setDinnerName(String dinnerName) {
		this.dinnerName = dinnerName;
	}
 

	public int getDinnerNameList() {
		return dinnerNameList;
	}

	public void setDinnerNameList(int dinnerNameList) {
		this.dinnerNameList = dinnerNameList;
	}


	private String peopleName;

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public int getPeopleSno() {
		return peopleSno;
	}

	public void setPeopleSno(int peopleSno) {
		this.peopleSno = peopleSno;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	private String isSingle; 

	public String getIsSingle() {
		return isSingle;
	}

	public void setIsSingle(String isSingle) {
		this.isSingle = isSingle;
	}

	public String getDinner() {
		return dinner;
	}

	public void setDinner(String dinner) {
		this.dinner = dinner;
	}

	private String dinner;

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

}
