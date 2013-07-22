package bean;

import java.io.Serializable;

public class Dinner implements Serializable {
	// 序列号
	private static final long serialVersionUID = 1L;
	private String dinnerName;
	private int times;
	private int sno;

	public int getSno() {
		return sno;
	}

	public String getDinnerName() {
		return dinnerName;
	}

	public void setDinnerName(String dinnerName) {
		this.dinnerName = dinnerName;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
