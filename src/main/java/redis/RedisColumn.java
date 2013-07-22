package redis;

/**
 * 本系统中使用到的全部的键值.
 * 
 * @author lsq
 * 
 */
public class RedisColumn {
	public final static String SYSTEM = "dinnerorder";
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

	private static class Key{
		private String k;
		private final static String SPLIT = ":";

		public Key(String k){
			this.k = k;
		}
		public Key add(String s){
			this.k+=SPLIT+s;
			return this;
		}
		public Key add(int s){
			this.k+=SPLIT+s;
			return this;
		}
		public byte[] getId(){
			return this.k.getBytes();
		}
	} 
	
	public static byte[] order() {
		return new Key(SYSTEM).add(ORDER_PRE).getId();
	}

	public static byte[] snoFactory() {
		return new Key(SYSTEM).add(SNO_FACTORY).getId();
	}

	public static byte[] peopleAndTimeToOrder(int people, String time) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(people).add(time).add(PEOPLE_AND_TIME_TO_ORDER).getId();
	}

	public static byte[] orderTime(int sno) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(sno).add(ORDER_TIME).getId();
	}

	/**
	 * 设置一个有权重的人员的集合. 用来计算最近的订餐人.
	 * 
	 * @param peolple
	 * @return
	 */
	public static byte[] orderPeopleWithScore() { 
		return new Key(SYSTEM).add(ORDER_PRE).add(ORDER_PEOPLE_WITH_SCORE).getId();
	}

	/**
	 * 设置订餐的权重的集合。
	 * 
	 * @return
	 */
	public static byte[] orderDinnerWithScore() { 
		return new Key(SYSTEM).add(ORDER_PRE).add(ORDER_DINNER_WITH_SCORE).getId();
	}

	/**
	 * 人员对应的全部订单.
	 * 
	 * @param people
	 * @return
	 */
	public static byte[] peopleToOrder(int people) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(people).add(ORDER_DINNER_WITH_SCORE).getId();
	}

	/**
	 * 时间对应的全部订单号.
	 * 
	 * @param time
	 * @return
	 */
	public static byte[] timeToOrder(String time) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(time).add(TIME_TO_ORDER).getId();
	}

	/**
	 * 得到有订单的全部时间的set.
	 * 
	 * @param time
	 * @return
	 */
	public static byte[] orderTimeSet() { 
		return new Key(SYSTEM).add(ORDER_PRE).add(ORDER_TIME_SET).getId();
	}

	public static byte[] orderMoney(int sno) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(sno).add(ORDER_MONEY).getId();
	}

	public static byte[] orderPeople(int sno) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(sno).add(ORDER_PEOPLE).getId();
	}

	public static byte[] orderSingle(int sno) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(sno).add(ORDER_SINGLE).getId();
	}

	public static byte[] orderDinner(int sno) { 
		return new Key(SYSTEM).add(ORDER_PRE).add(sno).add(ORDER_DINNER).getId();
	}

	public static byte[] orderList() { 
		return new Key(SYSTEM).add(ORDER_PRE).add(ORDER_LIST).getId();
	}

	public final static String LOGIN_PRE = "login";
	public final static String LOGIN_GROUP = "group";
	public final static String LOGIN_PASS = "pass";  
	public static byte[] login() { 
		return new Key(SYSTEM).add(LOGIN_PRE).getId();
	}
	 

	public static byte[] loginGroupName(int sno) { 
		return new Key(SYSTEM).add(LOGIN_PRE).add(sno).add(LOGIN_GROUP).getId();
	}

	public static byte[] loginPass(int sno) { 
		return new Key(SYSTEM).add(LOGIN_PRE).add(sno).add(LOGIN_PASS).getId();
	}
	
	public final static String MENU_PRE = "menu";
	public final static String MENU_NAME = "name";
	public final static String MENU_URL = "url";
	public final static String MENU_SNO = "sno";
	public final static String MENU_SET = "menus";
	public final static String MENU_CLICK = "menuclick";

	public static byte[] menu() { 
		return new Key(SYSTEM).add(MENU_PRE).getId();
	}

	public static byte[] menuName(int sno) { 
		return new Key(SYSTEM).add(MENU_PRE).add(sno).add(MENU_NAME).getId();
	}

	public static byte[] menuScore() { 
		return new Key(SYSTEM).add(MENU_PRE).add(MENU_CLICK).getId();
	}

	public static byte[] menuUrl(int sno) { 
		return new Key(SYSTEM).add(MENU_PRE).add(sno).add(MENU_URL).getId();
	}

	public static byte[] menuList() { 
		return new Key(SYSTEM).add(MENU_PRE).add(MENU_SET).getId();
	}

	public final static String DINNER_PRE = "dinner";
	public final static String DINNER_NAME = "name";
	public final static String DINNER_SNO = "sno";
	public final static String DINNER_LIST = "dinners";

	public static byte[] dinner() { 
		return new Key(SYSTEM).add(DINNER_PRE).getId();
	}

	public static byte[] dinnerName(int sno) { 
		return new Key(SYSTEM).add(DINNER_PRE).add(sno).add(DINNER_NAME).getId();
	}

	public static byte[] dinnerList() { 
		return new Key(SYSTEM).add(DINNER_PRE).add(DINNER_LIST).getId();
	}

	public final static String PEOPLE_PRE = "people";
	public final static String PEOPLE_NAME = "name";
	public final static String PEOPLE_SNO = "sno";
	public final static String PEOPLE_LIST = "peoples";

	public static byte[] people() { 
		return new Key(SYSTEM).add(PEOPLE_PRE).getId();
	}

	public static byte[] peopleName(int sno) { 
		return new Key(SYSTEM).add(PEOPLE_PRE).add(sno).add(PEOPLE_NAME).getId();
	}

	public static byte[] peopleList() { 
		return new Key(SYSTEM).add(PEOPLE_PRE).add(PEOPLE_LIST).getId();
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
		return new Key(SYSTEM).add(RECHARGE_PRE).add(people).add(PEOPLE_TO_RECHARGE).getId();
	}

	/**
	 * 人员对应的充值记录的金额. 保存为hashMap
	 * 
	 * @param people
	 * @return
	 */
	public static byte[] peopleToRechargeMoney() { 
		return new Key(SYSTEM).add(RECHARGE_PRE).add(PEOPLE_TO_RECHARGE_MONEY).getId();
	}

	/**
	 * 时间对应的充值记录. 充值的时候进行更新.
	 * 
	 * @param time
	 * @return
	 */
	public static byte[] timeToRecharge(String time) { 
		return new Key(SYSTEM).add(RECHARGE_PRE).add(time).add(TIME_TO_RECHARGE).getId();
	}

	public static byte[] recharge() { 
		return new Key(SYSTEM).add(RECHARGE_PRE).getId();
	}

	public static byte[] rechargePeople(int sno) { 
		return new Key(SYSTEM).add(RECHARGE_PRE).add(sno).add(RECHARGE_PEOPLE).getId();
	}

	public static byte[] rechargeTime(int sno) { 
		return new Key(SYSTEM).add(RECHARGE_PRE).add(sno).add(RECHARGE_TIME).getId();
	}

	public static byte[] rechargeList() { 
		return new Key(SYSTEM).add(RECHARGE_PRE).add(RECHARGE_LIST).getId();
	}

	public static byte[] rechargeMoney(int sno) { 
		return new Key(SYSTEM).add(RECHARGE_PRE).add(sno).add(RECHARGE_MONEY).getId();
	}

}
