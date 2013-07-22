package dinnerorder;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import bean.Dinner;
import bean.Menu;
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
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
	private String dinnerNameList;

	public String getDinnerNameList() {
		return dinnerNameList;
	}

	public void setDinnerNameList(String dinnerNameList) {
		this.dinnerNameList = dinnerNameList;
	}

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
		List<Dinner> dinners = dinner.getDinners();
		List<Menu> menus = dinner.getMenus();
		List<ReCharge> rechargs = dinner.getRecharges();
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("menus", menus);
		request.setAttribute("rechargs", rechargs);
		request.setAttribute("peoples", peoples);
		request.setAttribute("orders", orders);
		request.setAttribute("dinners", dinners);
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
		order.setDinnerName(dinnerName);
		order.setDinnerNameList(Integer.parseInt(dinnerNameList));
		order.setTime(moneyTime);
		order.setPeopleSno(Integer.parseInt(peopleList));
		order.setPeopleName(peopleName);
		dinner.saveOrder(order);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html;charset=GBK");
			response.getWriter().write("保存订单成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String menuName;
	private String menuUrl;

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String saveMenu() {
		Menu order = new Menu();
		order.setMenuName(menuName);
		order.setMenuUrl(menuUrl);
		dinner.saveMenu(order);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html;charset=GBK");
			response.getWriter().write("保存菜单成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String goUrl() {
		Menu m = dinner.clickMenu(Integer.parseInt(id));
		try {
			ServletActionContext.getResponse().sendRedirect(m.getMenuUrl());
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
			response.getWriter().write("充值成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String deleteOrder() {
		dinner.deleteOrder(id);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html;charset=GBK");
			response.getWriter().write("删除成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String deleteRecharge() {
		dinner.deleteRecharge(id);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html;charset=GBK");
			response.getWriter().write("删除成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
