package dinnerorder;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import bean.Order;
import bean.People;
import bean.ReCharge;

public class DinnerAction {
	// 序列号
	private static final long serialVersionUID = 1L;
	/**
	 * redis操作工具类.
	 */
	private IDinner dinner;
	private String moneyTime;
	private String peopleName;
	private String peopleList;
	private String rechargeMoneyTime;
	private String rechargePeopleList;
	private String rechargeMoney;

	public String getRechargeMoneyTime() { 
		return rechargeMoneyTime;
	}

	public void setRechargeMoneyTime(String rechargeMoneyTime) {
		this.rechargeMoneyTime = rechargeMoneyTime;
	}

	public String getRechargePeopleList() {
		return rechargePeopleList;
	}

	public void setRechargePeopleList(String rechargePeopleList) {
		this.rechargePeopleList = rechargePeopleList;
	}

	public String getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(String rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public String getPeopleList() {
		return peopleList;
	}

	public void setPeopleList(String peopleList) {
		this.peopleList = peopleList;
	}

	private String money;
	private String dinnerName;

	public String getDinnerName() {
		return dinnerName;
	}

	public void setDinnerName(String dinnerName) {
		this.dinnerName = dinnerName;
	}

	private String single;

	public IDinner getDinner() {
		return dinner;
	}

	public void setDinner(IDinner dinner) {
		this.dinner = dinner;
	}

	public String getMoneyTime() {
		return moneyTime;
	}

	public void setMoneyTime(String moneyTime) {
		this.moneyTime = moneyTime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getSingle() {
		return single;
	}

	public void setSingle(String single) {
		this.single = single;
	}

	public String init() {
		List<People> peoples = dinner.getPeoples(); 
		List<String> allTime = dinner.getOrderTimeSet(); 
		List<Order> orders = dinner.getOrders();
		List<ReCharge> rechargs = dinner.getRecharges();
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("peoples", peoples);
		request.setAttribute("rechargs", rechargs);
		request.setAttribute("orders", orders);
		request.setAttribute("allTime", allTime);
		return "dinner";
	}

	/**
	 * 保存订单信息.
	 * 
	 * @return
	 */
	public String saveOrder() {
		Order order = new Order();
		order.setDinner(dinnerName);
		order.setIsSingle(single);
		order.setMoney(money);
		order.setTime(moneyTime);
		order.setPeopleSno(Integer.parseInt(peopleList));
		order.setPeopleName(peopleName); 
		dinner.saveOrder(order);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html;charset=GBK");
			response.getWriter().write( "保存订单成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存充值记录.
	 * 
	 * @return
	 */
	public String saveRecharge() {
		ReCharge recharge = new ReCharge();
		recharge.setMoney(rechargeMoney);
		recharge.setTime(rechargeMoneyTime);
		recharge.setPeopleSno(Integer.parseInt(rechargePeopleList));
		dinner.saveRecharge(recharge);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html;charset=GBK");
			response.getWriter().write( "充值成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
