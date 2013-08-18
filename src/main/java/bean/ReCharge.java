package bean;

import java.io.Serializable;

import org.springframework.data.redis.connection.SortParameters;

/**
 * 充值记录.
 * 
 * @author lsq
 * 
 */
public class ReCharge implements Serializable, Comparable ,SortParameters{
	// 序列号
	private static final long serialVersionUID = 1L;
	private int peopleSno;
	private String time;
	private String money;
	private String groupSno;

	public String getGroupSno() {
		return groupSno;
	}

	public void setGroupSno(String groupSno) {
		this.groupSno = groupSno;
	}

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

	@Override
	public int compareTo(Object o) {
		int s = this.sno - ((ReCharge) o).getSno();
		if (s > 0)
			return 1;
		else if (s < 0)
			return -1;
		else
			return 0;
	}

	@Override
	public Order getOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isAlphabetic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getByPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[][] getGetPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Range getLimit() {
		// TODO Auto-generated method stub
		return null;
	}
}
