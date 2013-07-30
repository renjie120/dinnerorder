package bean;

import java.io.Serializable;

/**
 * 人员信息.
 * 
 * @author lsq
 * 
 */
public class People implements Serializable, Comparable {
	// 序列号
	private static final long serialVersionUID = 1L;
	private String name;
	private double rechargeSum;

	public double getRechargeSum() {
		return rechargeSum;
	}

	public void setRechargeSum(double rechargeSum) {
		this.rechargeSum = rechargeSum;
	}

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

	@Override
	public int compareTo(Object o) {
		double d = this.rechargeSum - ((People) o).getRechargeSum();
		if (d > 0) {
			return -1;
		} else if (d < 0) {
			return 1;
		} else{
			int s = this.sno - ((People) o).getSno();
			if(s>0)
				return 1;
			else if(s<0)
				return -1;
			else
				return 0; 
		}
	}
}
