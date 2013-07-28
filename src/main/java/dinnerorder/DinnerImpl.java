package dinnerorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

import redis.BaseRedisTool;
import redis.RedisColumn;
import bean.Dinner;
import bean.Login;
import bean.Menu;
import bean.Order;
import bean.People;
import bean.ReCharge;

/**
 * 进行订单的详情记录操作.
 * 
 * @author lsq
 * 
 */

@SuppressWarnings("unchecked")
public class DinnerImpl implements IDinner {
	private BaseRedisTool tool;

	public BaseRedisTool getTool() {
		return tool;
	}

	public void setTool(BaseRedisTool tool) {
		this.tool = tool;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveOrder(Order order) {
		final int val = tool.generateKey(RedisColumn.snoFactory());
		final Order p = order;
		// 如果添加的人员是-1，说明是新添加的人员信息，先保存到人员的缓存中去.
		final int peopleId;
		final int dinnerId;
		if (order.getPeopleSno() == -1) {
			People people = new People();
			people.setName(order.getPeopleName());
			peopleId = savePeople(people);
			order.setPeopleSno(peopleId);
		} else {
			peopleId = order.getPeopleSno();
		}
		if (order.getDinnerNameList() == -1) {
			Dinner people = new Dinner();
			people.setDinnerName(order.getDinnerName());
			dinnerId = saveDinner(people);
		} else {
			dinnerId = order.getDinnerNameList();
			// 查询对应的汉字显示名称.
			order.setDinner(tool.getKey(RedisColumn.dinnerName(dinnerId)));
		}
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {

					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(RedisColumn.orderDinner(val),
								(p.getDinner()).getBytes());
						connection.set(RedisColumn.orderMoney(val),
								(p.getMoney()).getBytes());
						connection.set(RedisColumn.orderPeople(val),
								(peopleId + "").getBytes());
						// 每添加一个菜单，就添加一个权重.
						connection.zIncrBy(RedisColumn.orderPeopleWithScore(),
								1, (peopleId + "").getBytes());
						// 添加一个菜名的权重.
						connection.zIncrBy(RedisColumn.orderDinnerWithScore(),
								1, (dinnerId + "").getBytes());
						connection.set(RedisColumn.orderSingle(val),
								(p.getIsSingle()).getBytes());
						// 添加分组对应的订单映射条件.
						connection.set(RedisColumn.orderGroup(val),
								(p.getGroupSno()).getBytes());
						connection.sAdd(RedisColumn.groupToPeople(Integer
								.parseInt(p.getGroupSno())),
								(p.getPeopleSno() + "").getBytes());
						// 添加当前分组的订单到一个集合中.
						connection.sAdd(RedisColumn.groupToOrder(Integer
								.parseInt(p.getGroupSno())), (val + "")
								.getBytes());

						connection.set(RedisColumn.orderTime(val),
								(p.getTime()).getBytes());
						connection.sAdd(RedisColumn.peopleToOrder(peopleId),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.timeToOrder(p.getTime()),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.groupToOrder(Integer.parseInt(p.getGroupSno())),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.orderList(),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.orderTimeSet(Integer.parseInt(p.getGroupSno())),
								(p.getTime()).getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);

		return val;
	}

	@Override
	public int saveRecharge(ReCharge recharge) {
		final int val = tool.generateKey(RedisColumn.snoFactory());
		final ReCharge p = recharge;
		byte[] m = tool.getHashByKey(RedisColumn.peopleToRechargeMoney(Integer.parseInt(p.getGroupSno())),
				(recharge.getPeopleSno() + "").getBytes());
		final byte[] sum;
		if (m == null)
			sum = recharge.getMoney().getBytes();
		else {
			double d = Double.parseDouble(new String(m));
			d += Double.parseDouble(recharge.getMoney());
			sum = (d + "").getBytes();
		}
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(RedisColumn.rechargePeople(val),
								(p.getPeopleSno() + "").getBytes());
						connection.set(RedisColumn.rechargeMoney(val), p
								.getMoney().getBytes());
						connection.set(RedisColumn.rechargeTime(val), p
								.getTime().getBytes());
						// 添加人员到充值记录的映射
						connection.sAdd(
								RedisColumn.peopleToRecharge(p.getPeopleSno()),
								(val + "").getBytes());
						// 添加分组对应的订单映射条件.
						connection.set(RedisColumn.rechargeGroup(val),
								(p.getGroupSno()).getBytes());
						connection.sAdd(RedisColumn.groupToRecharge(Integer
								.parseInt(p.getGroupSno())), (val + "")
								.getBytes());
						// 设置当前人的缴纳的定金总额.
						connection.hSet(RedisColumn.peopleToRechargeMoney(Integer.parseInt(p.getGroupSno())),
								(p.getPeopleSno() + "").getBytes(), sum);
						connection.sAdd(
								RedisColumn.timeToRecharge(p.getTime()),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.rechargeList(Integer.parseInt(p.getGroupSno())),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
		return val;
	}

	@Override
	public List<ReCharge> getRecharges( int groupSno) {
		Set ll = tool.getSet(RedisColumn.rechargeList(groupSno));
		List<ReCharge> result = new ArrayList<ReCharge>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			result.add(getReChargeById(key));
		}
		return result;
	}

	@Override
	public int savePeople(People order) {
		final int val = tool.generateKey(RedisColumn.snoFactory());
		final People p = order;
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(RedisColumn.peopleName(val), p.getName()
								.getBytes());
						connection.lPush(RedisColumn.peopleList(),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
		return val;
	}

	@Override
	public List<People> getPeoples() {
		List ll = tool.getList(RedisColumn.peopleList());
		List<People> result = new ArrayList<People>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			result.add(getPeopleById(key));
		}
		return result;
	}

	@Override
	public List<Order> getOrders() {
		Set<byte[]> ll = tool.getSet(RedisColumn.orderList());
		List<Order> result = new ArrayList<Order>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			result.add(getOrderById(key));
		}
		return result;
	}

	@Override
	public List<String> getOrderTimeSet(int groupSno) {
		Set<byte[]> s = tool.getSet(RedisColumn.orderTimeSet(groupSno));
		List<String> result = new ArrayList<String>();
		Iterator<byte[]> it = s.iterator();
		while (it.hasNext()) {
			result.add(new String(it.next()));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<String> getOrderByPeopleAndGroupInOneDay(int people, String time,int groupSno) {
		final int p = people;
		final String t = time;
		final int g =  groupSno ;  
		Set<byte[]> s = tool.interSet(RedisColumn.peopleToOrder(p), RedisColumn.timeToOrder(t),RedisColumn.groupToOrder(g));
		List<String> result = new ArrayList<String>();
		Iterator<byte[]> it = s.iterator();
		while (it.hasNext()) {
			result.add(new String(it.next()));
		}
		return result;
	}

	@Override
	public void deleteOrder(String id) {
		final int val = Integer.parseInt(id);
		final int peopleId = Integer.parseInt(tool.getKey(RedisColumn
				.orderPeople(val)));
		final int groupSno = Integer.parseInt(tool.getKey(RedisColumn
				.orderGroup(val)));
		final String time = tool.getKey(RedisColumn.orderTime(val));
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.del(RedisColumn.orderDinner(val));
						connection.del(RedisColumn.orderMoney(val));
						connection.del(RedisColumn.orderPeople(val));
						connection.del(RedisColumn.orderGroup(val));
						// 每添加一个菜单，就添加一个权重.
						connection.zIncrBy(RedisColumn.orderPeopleWithScore(),
								-1, (peopleId + "").getBytes());
						connection.del(RedisColumn.orderSingle(val));
						connection.sRem(RedisColumn.groupToOrder(groupSno),
								(val + "").getBytes());
						connection.del(RedisColumn.orderTime(val));
						connection.sRem(RedisColumn.peopleToOrder(peopleId),
								(val + "").getBytes());
						connection.sRem(RedisColumn.groupToOrder(groupSno),
								(val + "").getBytes());
						connection.sRem(RedisColumn.timeToOrder(time),
								(val + "").getBytes());
						connection.sRem(RedisColumn.orderList(),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
	}

	@Override
	public void deletePeople(String id) {

	}

	@Override
	public void deleteRecharge(String id) {
		final int val = Integer.parseInt(id);
		final int people = Integer.parseInt(tool.getKey(RedisColumn
				.rechargePeople(val)));
		final int groupSno = Integer.parseInt(tool.getKey(RedisColumn
				.rechargeGroup(val)));
		final String time = tool.getKey(RedisColumn.rechargeTime(val));
		double money = Double.parseDouble(tool.getKey(RedisColumn.rechargeMoney(val)));
		byte[] m = tool.getHashByKey(RedisColumn.peopleToRechargeMoney(groupSno),
				(people+ "").getBytes());
		final byte[] sum;		 
		double d = Double.parseDouble(new String(m));
		d -= money;
		sum = (d + "").getBytes();
		 
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.del(RedisColumn.rechargePeople(val));
						connection.del(RedisColumn.rechargeMoney(val));
						connection.del(RedisColumn.rechargeTime(val));
						connection.del(RedisColumn.rechargeGroup(val));
						connection.sRem(RedisColumn.groupToRecharge(groupSno),
								(val + "").getBytes());
						// 添加人员到充值记录的映射
						connection.sRem(RedisColumn.peopleToRecharge(people),
								(val + "").getBytes());
						connection.hSet(RedisColumn.peopleToRechargeMoney(groupSno),
								(people + "").getBytes(), sum);
						connection.sRem(RedisColumn.timeToRecharge(time),
								(val + "").getBytes());
						connection.sRem(RedisColumn.rechargeList(groupSno),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
	}

	@Override
	public int saveMenu(Menu order) {
		final int val = tool.generateKey(RedisColumn.snoFactory());
		final Menu p = order;
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(RedisColumn.menuName(val), p
								.getMenuName().getBytes());
						connection.set(RedisColumn.menuUrl(val), p.getMenuUrl()
								.getBytes());
						connection.lPush(RedisColumn.menuList(),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
		return val;
	}

	@Override
	public List<Menu> getMenus() {
		List<byte[]> ll = tool.getList(RedisColumn.menuList());
		List<Menu> result = new ArrayList<Menu>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			result.add(getMenuById(key));
		}
		return result;
	}

	@Override
	public Menu clickMenu(int key) {
		Menu p = new Menu();
		final String k = key + "";
		p.setMenuName(tool.getKey(RedisColumn.menuName(key)));
		p.setMenuUrl(tool.getKey(RedisColumn.menuUrl(key)));
		tool.getTemplate().execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				// 添加一次点击数.
				connection.zIncrBy(RedisColumn.menuScore(), 1, k.getBytes());
				return null;
			}

		});
		p.setSno(key);
		return p;
	}

	@Override
	public List<Dinner> getDinners() {
		Set<byte[]> ll = tool.getSet(RedisColumn.dinnerList());
		List<Dinner> result = new ArrayList<Dinner>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			Dinner p = new Dinner();
			p.setDinnerName(tool.getKey(RedisColumn.dinnerName(key)));
			p.setSno(key);
			result.add(p);
		}
		return result;
	}

	@Override
	public int saveDinner(Dinner order) {
		final int val = tool.generateKey(RedisColumn.snoFactory());
		final Dinner p = order;
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(RedisColumn.dinnerName(val), p
								.getDinnerName().getBytes());
						connection.sAdd(RedisColumn.dinnerList(),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
		return val;
	}

	@Override
	public List<Dinner> getDinnersByRank(int top) {
		List<Dinner> result = new ArrayList<Dinner>();
		Set<Tuple> s = tool.getListWithScore(
				RedisColumn.orderDinnerWithScore(), top);
		Iterator<Tuple> itt = s.iterator();
		while (itt.hasNext()) {
			Tuple tt = itt.next();
			int _pid = Integer.parseInt(new String(tt.getValue()));
			Dinner d = getDinnerById(_pid);
			d.setScore(tt.getScore().intValue());
			result.add(d);
		}
		return result;
	}

	@Override
	public List<People> getPeopleByRechargesRank(int groupSno) {
		List<People> result = new ArrayList<People>();
		Map<byte[], byte[]> s = tool.getMapAll(RedisColumn
				.peopleToRechargeMoney(groupSno));
		Iterator<Map.Entry<byte[], byte[]>> it = s.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<byte[], byte[]> m = it.next();
			int pId = Integer.parseInt(new String(m.getKey()));
			People p = getPeopleById(pId);
			p.setRechargeSum(Double.parseDouble(new String(m.getValue())));
			result.add(p);
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public Double getSumByDay(String time,int groupSno) {
		Set<byte[]> s = tool.interSet(RedisColumn.timeToOrder(time),RedisColumn.groupToOrder(groupSno));
		double sum = 0;
		for (Object o : s) {
			int key = Integer.parseInt(new String((byte[]) o));
			sum += Double.parseDouble(tool.getKey(RedisColumn.orderMoney(key)));
		}
		return sum;
	}

	@Override
	public Dinner getDinnerById(int tim) {
		Dinner p = new Dinner();
		p.setDinnerName(tool.getKey(RedisColumn.dinnerName(tim)));
		p.setSno(tim);
		return p;
	}

	@Override
	public Order getOrderById(int key) {
		Order p = new Order();
		p.setDinner(tool.getKey(RedisColumn.orderDinner(key)));
		p.setIsSingle(tool.getKey(RedisColumn.orderSingle(key)));
		p.setMoney(tool.getKey(RedisColumn.orderMoney(key)));
		p.setPeopleSno(Integer.parseInt(tool.getKey(RedisColumn
				.orderPeople(key))));
		p.setSno(key);
		p.setTime(tool.getKey(RedisColumn.orderTime(key)));
		return p;
	}

	@Override
	public People getPeopleById(int tim) {
		People p = new People();
		p.setName(tool.getKey(RedisColumn.peopleName(tim)));
		p.setSno(tim);
		return p;
	}

	@Override
	public Menu getMenuById(int tim) {
		Menu p = new Menu();
		p.setMenuName(tool.getKey(RedisColumn.menuName(tim)));
		p.setClickTimes((int) tool.getScore(RedisColumn.menuScore(),
				(tim + "").getBytes()));
		p.setMenuUrl(tool.getKey(RedisColumn.menuUrl(tim)));
		p.setSno(tim);
		return p;
	}

	@Override
	public ReCharge getReChargeById(int key) {
		ReCharge p = new ReCharge();
		p.setMoney(tool.getKey(RedisColumn.rechargeMoney(key)));
		p.setPeopleSno(Integer.parseInt(tool.getKey(RedisColumn
				.rechargePeople(key))));
		p.setSno(key);
		p.setTime(tool.getKey(RedisColumn.rechargeTime(key)));
		return p;
	}

	@Override
	public int login(Login param) {
		if (param.getSno() == -1) {
			return regiest(param);
		} else {
			int userId = param.getSno();
			if (param.getPass().equals(
					tool.getKey(RedisColumn.loginPass(userId)))) {
				return param.getSno();
			} else {
				return -1;
			}
		}
	}

	@Override
	public int regiest(Login param) {
		final int val = tool.generateKey(RedisColumn.snoFactory());
		final Login p = param;
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(RedisColumn.loginGroupName(val), p
								.getGroupName().getBytes());
						connection.set(RedisColumn.loginPass(val), p.getPass()
								.getBytes());
						connection.sAdd(RedisColumn.login(),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
		return val;
	}

	@Override
	public Login getLoginById(int tim) {
		Login p = new Login();
		p.setGroupName(tool.getKey(RedisColumn.loginGroupName(tim)));
		p.setSno(tim);
		return p;
	}

	@Override
	public List<Login> getLogins() {
		Set<byte[]> ll = tool.getSet(RedisColumn.login());
		List<Login> result = new ArrayList<Login>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			result.add(getLoginById(key));
		}
		return result;
	}

	@Override
	public List<String> getOrderByGroupAndDay(int groupSno, String time) {
		final int p = groupSno;
		final String t = time;  
		Set<byte[]> s = tool.interSet(RedisColumn.groupToOrder(p), RedisColumn.timeToOrder(t));
		List<String> result = new ArrayList<String>();
		Iterator<byte[]> it = s.iterator();
		while (it.hasNext()) {
			result.add(new String(it.next()));
		}
		return result;
	}

	@Override
	public List<String> getRechargeByGroup(int group) {
		Set<byte[]> thisGroupRecharge = tool.getSet(RedisColumn
				.groupToRecharge(group));
		List<String> result = new ArrayList<String>();
		Iterator<byte[]> it = thisGroupRecharge.iterator();
		while (it.hasNext()) {
			result.add(new String(it.next()));
		}
		return result;
	}

	@Override
	public void saveArg(String time, double money, int groupSno) {
		final int g = groupSno;
		final String t = time;
		final String m = money + "";
		// 得到当天的全部订单
		List<String> orders = getOrderByGroupAndDay(groupSno, time);
		final List<Order> _ods = new ArrayList<Order>();
		for (String _o : orders) {
			Order oo = new Order();
			oo.setSno(Integer.parseInt(_o));
			oo.setIsSingle(tool.getKey(RedisColumn.orderSingle(Integer
					.parseInt(_o))));
			oo.setMoney(tool.getKey(RedisColumn.orderMoney(Integer.parseInt(_o))));
			_ods.add(oo);
		}
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {

					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set(RedisColumn.timeAndGroupToAvg(t, g),
								m.getBytes());
						// 设置全部的当天的消费值.
						for (Order o : _ods) {
							// 如果不是单点，该订单的消费额就是平均值.
							if ("0".equals(o.getIsSingle()))
								connection.set(
										RedisColumn.orderCost(o.getSno()),
										m.getBytes());
							else {
								// 单点的话消费额就是该订单的金额，而不是平均值.
								connection.set(
										RedisColumn.orderCost(o.getSno()), o
												.getMoney().getBytes());
							}
						}
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		tool.getTemplate().execute(sessionCallback);
	}

	@Override
	public List<People> getPeopleByCostsRank(final int group) {
		Set<byte[]> allPeoples = tool.getSet(RedisColumn.groupToPeople(group));
		List<People> result = new ArrayList<People>();
		for (byte[] p : allPeoples) { 
			Set<byte[]> orders = getOrderByGroupAndPeople(group,Integer.parseInt(new String(p)));
			People _p = new People();
			_p.setSno(Integer.parseInt(new String(p)));
			_p.setName(tool.getKey(RedisColumn.peopleName(_p.getSno())));
			if("米饭".equals(_p.getName())){
				continue;
			}
			double sum = 0;
			for (byte[] o:orders){
				String cost = tool.getKey(RedisColumn.orderCost(Integer.parseInt(new String(o))));
				String cost2 = tool.getKey(RedisColumn.orderMoney(Integer.parseInt(new String(o))));
				//如果没有设置平均消费，就从实际点餐金额中计算.
				if(cost!=null)
					sum+=Double.parseDouble(cost);
				else
					sum+=Double.parseDouble(cost2);
			}
			_p.setRechargeSum(sum);
			result.add(_p);
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<People> getPeopleByGroup(int groupSno) {
		Set<byte[]> allPeoples = tool.getSet(RedisColumn.groupToPeople(groupSno));
		List<People> result = new ArrayList<People>();
		for (byte[] p : allPeoples) { 
			 People _p = new People();
			_p.setSno(Integer.parseInt(new String(p)));
			_p.setName(tool.getKey(RedisColumn.peopleName(_p.getSno()))); 
			result.add(_p);
		} 
		return result;
	}

	@Override
	public Set<byte[]> getOrderByGroupAndPeople(int groupSno, int people) {
		final int g = groupSno;
		final int p = people; 
		return tool.interSet(RedisColumn.groupToOrder(g),RedisColumn.peopleToOrder(p));
	}

	@Override
	public List<People> peopleByQianfeiMoneyRank(int groupSno) {
		List<People> costrank  = getPeopleByCostsRank(groupSno);
		Map<byte[], byte[]> s = tool.getMapAll(RedisColumn
				.peopleToRechargeMoney(groupSno));
		for (People p : costrank) {
			if(s.get((p.getSno()+"").getBytes())!=null){
				double chargeMoney = Double.parseDouble(new String(s.get((p.getSno()+"").getBytes())));
				p.setRechargeSum(p.getRechargeSum()-chargeMoney);
			} else{
				p.setRechargeSum(p.getRechargeSum());
			}
		}
		Collections.sort(costrank);
		return costrank;
	}

}
