package dinnerorder;

import java.util.List;
import java.util.Set;

import bean.Dinner;
import bean.Login;
import bean.Menu;
import bean.Order;
import bean.People;
import bean.ReCharge;

public interface IDinner {
	/**
	 * 保存订单.
	 * 返回流水号.
	 * @param order
	 */
	public int saveOrder(Order order);
	/**
	 * 添加菜单.
	 * @param order
	 * @return
	 */
	public int saveMenu(Menu order);

	/**
	 * 返回全部的菜单.
	 * @return
	 */
	public List<Menu> getMenus();

	public List<Login> getLogins();

	/**
	 * 返回菜单详情.
	 * @param id
	 * @return
	 */
	public  Menu  clickMenu(int id);

	/**
	 * 保存充值记录.
	 * 返回流水号.
	 * @param recharge
	 */
	public int saveRecharge(ReCharge recharge); 

	/**
	 * 查询充值记录.
	 * @return
	 */
	public List<ReCharge> getRecharges();

	/**
	 * 保存人员信息.
	 * 返回流水号.
	 * @param order
	 */
	public int savePeople(People p);

	/**
	 * 保存菜名.
	 * @param p
	 * @return
	 */
	public int saveDinner(Dinner p);

	
	/**
	 * 返回人员信息.
	 * @return
	 */
	public List<People> getPeoples();
	 

	/**
	 * 返回下了订单的日期.
	 * @return
	 */
	public List<String> getOrderTimeSet();

	/**
	 * 返回某人在某天的下的订单号.
	 * @param people
	 * @param time
	 * @return
	 */
	public List<String> getOrderByPeopleInOneDay(int people,String time);

	/**
	 * 得到某个分组的充值记录.
	 * @param time
	 * @return
	 */
	public List<String> getRechargeByGroup(int group);

	
	/**
	 * 返回当天含有的分组里面的订单.
	 * @param groupSno
	 * @param time
	 * @return
	 */
	public List<String> getOrderByGroupAndDay(int groupSno,String time);

	/**
	 * 返回订单记录.
	 * @return
	 */
	public List<Order> getOrders();

	/**
	 * 返回全部的菜名
	 * @return
	 */
	public List<Dinner> getDinners(); 
	
	/**
	 * 删除订单.
	 * @param order
	 */
	public void deleteOrder(String id);
	
	/**
	 * 登录并注册.
	 * 如果返回-1就验证失败，否则就是返回的用户id.
	 * @param param
	 */
	public int login(Login param);
	
 
	/**
	 * 注册.
	 * @param param
	 * @return
	 */
	public int  regiest(Login param);
	
	/**
	 * 删除人员信息.
	 * @param order
	 */
	public void deletePeople(String id);
	
	/**
	 * 删除充值记录.
	 * @param recharge
	 */
	public void deleteRecharge(String id); 
	
	/**
	 * 得到点菜数量排行榜
	 * @return
	 */
	public List<Dinner> getDinnersByRank(); 
	
	/**
	 * 得到人员的充值记录排行榜.
	 * @return
	 */
	public List<People> getPeopleByRechargesRank(); 
	  
	/**
	 * 得到某天的订餐金额总量.
	 * @param tim
	 * @return
	 */
	public Double getSumByDay(String tim); 
	
	/**
	 * 
	 * @param tim
	 * @return
	 */
	public Dinner getDinnerById(int tim);
	
	public Order getOrderById(int tim);
	
	public People getPeopleById(int tim);
	
	public Menu getMenuById(int tim);
	
	public Login getLoginById(int tim);
	
	public ReCharge getReChargeById(int tim);
}
