package dinnerorder;

import java.util.List;

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
	 * 保存充值记录.
	 * 返回流水号.
	 * @param recharge
	 */
	public int saveRecharge(ReCharge recharge);

	/**
	 * 删除充值记录.
	 * @param recharge
	 */
	public void deleteRecharge(ReCharge recharge);

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
	 * 删除人员信息.
	 * @param order
	 */
	public void deletePeople(People order);

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
	 * 返回订单记录.
	 * @return
	 */
	public List<Order> getOrders();

	/**
	 * 删除订单.
	 * @param order
	 */
	public void deleteOrder(Order order);
}
