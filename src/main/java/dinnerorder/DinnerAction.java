package dinnerorder;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import bean.Dinner;
import bean.Login;
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
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupSno = "" + request.getSession().getAttribute("groupSno");
		if (groupSno != null && !"null".equals(groupSno)) {
			int _groupSno = Integer.parseInt(groupSno);
			List<People> peoples = dinner.getPeopleByGroup(_groupSno);
			request.setAttribute("peoples", peoples);
			List<String> allTime = dinner.getOrderTimeSet(_groupSno);
			request.setAttribute("allTime", allTime);
			List<ReCharge> rechargs = dinner.getRecharges(_groupSno);
			request.setAttribute("rechargs", rechargs);
		}
		List<Dinner> dinners = dinner.getDinners();
		List<Menu> menus = dinner.getMenus();
		request.setAttribute("menus", menus); 
		request.setAttribute("dinners", dinners);
		return "dinner";
	}

	private String groupSno;

	public String getGroupSno() {
		return groupSno;
	}

	public void setGroupSno(String groupSno) {
		this.groupSno = groupSno;
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
		order.setGroupSno(groupSno);
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

	/**
	 * 保存菜单.
	 * 
	 * @return
	 */
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

	public String setAvg() {
		dinner.saveArg(moneyTime, Double.parseDouble(money),
				Integer.parseInt(groupSno));
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/html;charset=GBK");
			response.getWriter().write("保存成功!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据充值金额排序的人员清单.
	 * 
	 * @return
	 */
	public String peopleByRechargeMoneyRank() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupSno = "" + request.getSession().getAttribute("groupSno");
		List<People> p = dinner.getPeopleByRechargesRank(Integer.parseInt(groupSno)); 
		request.setAttribute("rankPeople", p);
		request.setAttribute("title", "充值金额统计排行榜");
		return "newTable";
	}
	
	/**
	 * 
	 * 
	 * <pre>
	 * 返回一个人的全部的订单记录.
	 * </pre>
	 * @return
	 */
	public String getPeopleOrders() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupSno = "" + request.getSession().getAttribute("groupSno");
		String peopleSno = request.getParameter("peopleSno");
		List<Order> p = dinner.getOrdersByPeople(Integer.parseInt(groupSno),Integer.parseInt(peopleSno)); 
		request.setAttribute("peopleOrders", p);
		request.setAttribute("title", "消费记录");
		return "newTable";
	}

	public String peopleByCostMoneyRank() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupSno = "" + request.getSession().getAttribute("groupSno");
		List<People> p = dinner
				.getPeopleByCostsRank(Integer.parseInt(groupSno));
		request.setAttribute("rankPeople", p);
		request.setAttribute("title", "消费金额统计排行榜");
		return "newTable";
	}

	public String peopleByQianfeiMoneyRank() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String groupSno = "" + request.getSession().getAttribute("groupSno");
		List<People> p = dinner.peopleByQianfeiMoneyRank(Integer
				.parseInt(groupSno));
		request.setAttribute("rankPeople", p);
		request.setAttribute("title", "欠费统计排行榜");
		return "newTable";
	}

	/**
	 * 根据菜名的订单数的清单.
	 * 
	 * @return
	 */
	public String dinnerRank() {
		List<Dinner> p = dinner.getDinnersByRank(10);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("rankDinner", p);
		request.setAttribute("title", "大家最喜欢订餐排行榜");
		return "newTable";
	}

	/**
	 * 点击url
	 * 
	 * @return
	 */
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
		recharge.setGroupSno(groupSno);
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

	/**
	 * 删除订单.
	 * 
	 * @return
	 */
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

	private String groupNameList;

	public String getGroupNameList() {
		return groupNameList;
	}

	public void setGroupNameList(String groupNameList) {
		this.groupNameList = groupNameList;
	}

	private String groupName;
	private String pass;

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

	public String login() {
		Login l = new Login();
		l.setGroupName(groupName);
		l.setSno(Integer.parseInt(groupNameList));
		l.setPass(pass);
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		int userId = dinner.login(l);
		if (userId > 0) {
			Login lll = dinner.getLoginById(userId);
			request.getSession().setAttribute("groupName", lll.getGroupName());
			request.getSession().setAttribute("userNo", lll.getSno());
			request.getSession().setAttribute("groupSno", lll.getSno());
			try {
				response.setContentType("text/html;charset=GBK");
				response.getWriter().write(
						"欢迎进入\"" + lll.getGroupName() + "\"订餐界面!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Login lll = dinner
						.getLoginById(Integer.parseInt(groupNameList));
				request.getSession().setAttribute("userNo", "-1");
				request.getSession().setAttribute("groupSno", lll.getSno());
				request.getSession().setAttribute("groupName",
						lll.getGroupName());
				response.setContentType("text/html;charset=GBK");
				response.getWriter().write(
						"验证失败，将以游客身份登陆\"" + lll.getGroupName() + "\"订餐界面!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 删除充值记录.
	 * 
	 * @return
	 */
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
