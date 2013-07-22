package redis;

/**
 * 本系统中使用到的全部的键值.
 * 
 * @author lsq
 * 
 */
public class RedisColumn {
	public final static String SPLIT = ":";
	public final static String SYSTEM = "dinnerorder:";
	public final static String SNO_FACTORY = "diner_sno";
	public final static String ORDER_PRE = "order";
	public final static String ORDER_TIME = "timer";
	public final static String ORDER_MONEY = "money";
	public final static String ORDER_PEOPLE = "people";
	public final static String ORDER_SNO = "sno";
	public final static String ORDER_SINGLE = "single";
	public final static String ORDER_DINNER = "dinner";
	public final static String ORDER_LIST = "all_sno";
	public final static String PEOPLE_TO_ORDER = "peopleToOrder";
	public final static String PEOPLE_AND_TIME_TO_ORDER = "peopleAndTimeToOrder";
	public final static String TIME_TO_ORDER = "timeToOrder";
	public final static String ORDER_TO_TIME = "orderToTime";
	public final static String ORDER_PEOPLE_WITH_SCORE = "peopleWithScore";
	public final static String ORDER_DINNER_WITH_SCORE = "dinnerWithScore";
	public final static String ORDER_TIME_SET = "order_times_set";
	public final static String ORDER_SUM_MONEY = "sumMoney";

	public static byte[] order() {
		return (SYSTEM + ORDER_PRE).getBytes();
	}

	public static byte[] snoFactory() {
		return (SYSTEM + SNO_FACTORY).getBytes();
	}

	public static byte[] peopleAndTimeToOrder(int people, String time) {
		return (SYSTEM + ORDER_PRE + SPLIT + people + SPLIT + time + SPLIT + PEOPLE_AND_TIME_TO_ORDER)
				.getBytes();
	}

	public static byte[] orderTime(int sno) {
		return (SYSTEM + ORDER_PRE + SPLIT + sno + SPLIT + ORDER_TIME)
				.getBytes();
	}

	/**
	 * 设置一个有权重的人员的集合. 用来计算最近的订餐人.
	 * 
	 * @param peolple
	 * @return
	 */
	public static byte[] orderPeopleWithScore() {
		return (SYSTEM + ORDER_PRE + SPLIT + ORDER_PEOPLE_WITH_SCORE)
				.getBytes();
	}
	
	/**
	 * 设置订餐的权重的集合。
	 * @return
	 */
	public static byte[] orderDinnerWithScore() {
		return (SYSTEM + ORDER_PRE + SPLIT + ORDER_DINNER_WITH_SCORE)
				.getBytes();
	}
	
	/**
	 * 人员对应的全部订单.
	 * 
	 * @param people
	 * @return
	 */
	public static byte[] peopleToOrder(int people) {
		return (SYSTEM + ORDER_PRE + SPLIT + people + SPLIT + PEOPLE_TO_ORDER)
				.getBytes();
	}

	/**
	 * 时间对应的全部订单号.
	 * 
	 * @param time
	 * @return
	 */
	public static byte[] timeToOrder(String time) {
		return (SYSTEM + ORDER_PRE + SPLIT + time + SPLIT + TIME_TO_ORDER)
				.getBytes();
	}

	/**
	 * 得到有订单的全部时间的set.
	 * 
	 * @param time
	 * @return
	 */
	public static byte[] orderTimeSet() {
		return (SYSTEM + ORDER_PRE + SPLIT + ORDER_TIME_SET).getBytes();
	}

	public static byte[] orderMoney(int sno) {
		return (SYSTEM + ORDER_PRE + SPLIT + sno + SPLIT + ORDER_MONEY)
				.getBytes();
	}

	public static byte[] orderPeople(int sno) {
		return (SYSTEM + ORDER_PRE + SPLIT + sno + SPLIT + ORDER_PEOPLE)
				.getBytes();
	}

	public static byte[] orderSingle(int sno) {
		return (SYSTEM + ORDER_PRE + SPLIT + sno + SPLIT + ORDER_SINGLE)
				.getBytes();
	}

	public static byte[] orderDinner(int sno) {
		return (SYSTEM + ORDER_PRE + SPLIT + sno + SPLIT + ORDER_DINNER)
				.getBytes();
	}

	public static byte[] orderList() {
		return (SYSTEM + ORDER_PRE + SPLIT + ORDER_LIST).getBytes();
	}

	public final static String MENU_PRE = "menu";
	public final static String MENU_NAME = "name";
	public final static String MENU_URL = "url";
	public final static String MENU_SNO = "sno";
	public final static String MENU_SET = "menus";
	public final static String MENU_CLICK = "menuclick";

	public static byte[] menu() {
		return (SYSTEM + SPLIT + MENU_PRE).getBytes();
	}

	public static byte[] menuName(int sno) {
		return (SYSTEM + SPLIT + MENU_PRE + SPLIT + sno + SPLIT + MENU_NAME)
				.getBytes();
	}

	public static byte[] menuScore() {
		return (SYSTEM + SPLIT + MENU_PRE + SPLIT + MENU_CLICK).getBytes();
	}

	public static byte[] menuUrl(int sno) {
		return (SYSTEM + SPLIT + MENU_PRE + SPLIT + sno + SPLIT + MENU_URL)
				.getBytes();
	}

	public static byte[] menuList() {
		return (SYSTEM + SPLIT + MENU_PRE + SPLIT + MENU_SET).getBytes();
	}

	public final static String DINNER_PRE = "dinner";
	public final static String DINNER_NAME = "name";
	public final static String DINNER_SNO = "sno";
	public final static String DINNER_LIST = "dinners";

	public static byte[] dinner() {
		return (SYSTEM + SPLIT + DINNER_PRE).getBytes();
	}

	public static byte[] dinnerName(int sno) {
		return (SYSTEM + SPLIT + DINNER_PRE + SPLIT + sno + SPLIT + DINNER_NAME)
				.getBytes();
	}

	public static byte[] dinnerList() {
		return (SYSTEM + SPLIT + DINNER_PRE + SPLIT + DINNER_LIST).getBytes();
	}

	public final static String PEOPLE_PRE = "people";
	public final static String PEOPLE_NAME = "name";
	public final static String PEOPLE_SNO = "sno";
	public final static String PEOPLE_LIST = "peoples";

	public static byte[] people() {
		return (SYSTEM + PEOPLE_PRE).getBytes();
	}

	public static byte[] peopleName(int sno) {
		return (SYSTEM + PEOPLE_PRE + SPLIT + sno + SPLIT + PEOPLE_NAME)
				.getBytes();
	}

	public static byte[] peopleList() {
		return (SYSTEM + PEOPLE_PRE + SPLIT + PEOPLE_LIST).getBytes();
	}

	public final static String RECHARGE_SNO = "sno";
	public final static String RECHARGE_PRE = "recharge";
	public final static String RECHARGE_LIST = "recharges";
	public final static String PEOPLE_TO_RECHARGE = "peopleToRecharge";
	public final static String PEOPLE_TO_RECHARGE_MONEY = "peopleToRechargeMoney";
	public final static String TIME_TO_RECHARGE = "timeToRecharge";
	public final static String RECHARGE_PEOPLE = "people";
	public final static String RECHARGE_TIME = "time";
	public final static String RECHARGE_MONEY = "money";

	/**
	 * 人员对应的充值记录. 在充值的时候进行添加.
	 * 
	 * @param people
	 * @return
	 */
	public static byte[] peopleToRecharge(int people) {
		return (SYSTEM + RECHARGE_PRE + SPLIT + people + SPLIT + PEOPLE_TO_RECHARGE)
				.getBytes();
	}

	/**
	 * 时间对应的充值记录. 充值的时候进行更新.
	 * 
	 * @param time
	 * @return
	 */
	public static byte[] timeToRecharge(String time) {
		return (SYSTEM + RECHARGE_PRE + SPLIT + time + SPLIT + TIME_TO_RECHARGE)
				.getBytes();
	}

	public static byte[] recharge() {
		return (SYSTEM + RECHARGE_PRE).getBytes();
	}

	public static byte[] rechargePeople(int sno) {
		return (SYSTEM + RECHARGE_PRE + SPLIT + sno + SPLIT + RECHARGE_PEOPLE)
				.getBytes();
	}

	public static byte[] rechargeTime(int sno) {
		return (SYSTEM + RECHARGE_PRE + SPLIT + sno + SPLIT + RECHARGE_TIME)
				.getBytes();
	}

	public static byte[] rechargeList() {
		return (SYSTEM + PEOPLE_PRE + SPLIT + RECHARGE_LIST).getBytes();
	}

	public static byte[] rechargeMoney(int sno) {
		return (SYSTEM + RECHARGE_PRE + SPLIT + sno + SPLIT + RECHARGE_MONEY)
				.getBytes();
	}

}
