package dinnerorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

import redis.BaseRedisTool;
import redis.RedisColumn;
import bean.Dinner;
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
		} else {
			peopleId = order.getPeopleSno();
		}
		if (order.getDinnerNameList() == -1) {
			Dinner people = new Dinner();
			people.setDinnerName(order.getDinnerName());
			dinnerId = saveDinner(people);
		} else {
			dinnerId = order.getDinnerNameList();
			//查询对应的汉字显示名称.
			order.setDinner(tool.getKey(RedisColumn.dinnerName(dinnerId)));
		}
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback(){

					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException { 
						connection.set(RedisColumn.orderDinner(val),
								(p.getDinner()).getBytes());
						connection.set(RedisColumn.orderMoney(val),
								(p.getMoney()).getBytes());
						connection.set(RedisColumn.orderPeople(val),
								(peopleId + "").getBytes());
						//每添加一个菜单，就添加一个权重.
						connection.zIncrBy(RedisColumn.orderPeopleWithScore(), 1, (peopleId + "").getBytes());
						//添加一个菜名的权重.
						connection.zIncrBy(RedisColumn.orderDinnerWithScore(), 1, (dinnerId+"").getBytes());
						connection.set(RedisColumn.orderSingle(val),
								(p.getIsSingle()).getBytes());
						connection.set(RedisColumn.orderTime(val),
								(p.getTime()).getBytes());  
						connection.sAdd(RedisColumn.peopleToOrder(peopleId),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.timeToOrder(p.getTime()),
								(val + "").getBytes()); 
						connection.sAdd(RedisColumn.orderList(),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.orderTimeSet(),
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
						connection.sAdd(
								RedisColumn.timeToRecharge(p.getTime()),
								(val + "").getBytes());
						connection.sAdd(RedisColumn.rechargeList(),
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
	public List<ReCharge> getRecharges() {
		Set ll = tool.getSet(RedisColumn.rechargeList());
		List<ReCharge> result = new ArrayList<ReCharge>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			ReCharge p = new ReCharge();
			p.setMoney(tool.getKey(RedisColumn.rechargeMoney(key)));
			p.setPeopleSno(Integer.parseInt(tool.getKey(RedisColumn
					.rechargePeople(key))));
			p.setSno(key);
			p.setTime(tool.getKey(RedisColumn.rechargeTime(key)));
			result.add(p);
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
			People p = new People();
			p.setName(tool.getKey(RedisColumn.peopleName(key)));
			p.setSno(key);
			result.add(p);
		}
		return result;
	}

	@Override
	public List<Order> getOrders() {
		Set<byte[]> ll = tool.getSet(RedisColumn.orderList());
		List<Order> result = new ArrayList<Order>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			Order p = new Order();
			p.setDinner(tool.getKey(RedisColumn.orderDinner(key)));
			p.setIsSingle(tool.getKey(RedisColumn.orderSingle(key)));
			p.setMoney(tool.getKey(RedisColumn.orderMoney(key)));
			p.setPeopleSno(Integer.parseInt(tool.getKey(RedisColumn
					.orderPeople(key))));
			p.setSno(key);
			p.setTime(tool.getKey(RedisColumn.orderTime(key)));
			result.add(p);
		}
		return result;
	}

	@Override
	public List<String> getOrderTimeSet() {
		Set<byte[]> s = tool.getSet(RedisColumn.orderTimeSet());
		List<String> result = new ArrayList<String>();
		Iterator<byte[]> it = s.iterator();
		while (it.hasNext()) {
			result.add(new String(it.next()));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<String> getOrderByPeopleInOneDay(int people, String time) {
		final int p = people;
		final String t = time;
		// 添加一个焦急
		tool.getTemplate().execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				// 保存在一个交集中.
				connection.sInterStore(RedisColumn.peopleAndTimeToOrder(p, t),
						RedisColumn.peopleToOrder(p),
						RedisColumn.timeToOrder(t));
				return null;
			}

		});

		Set<byte[]> s = tool.getSet(RedisColumn.peopleAndTimeToOrder(p, t));
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
						// 每添加一个菜单，就添加一个权重.
						connection.zIncrBy(RedisColumn.orderPeopleWithScore(),
								-1, (peopleId + "").getBytes());
						connection.del(RedisColumn.orderSingle(val));
						connection.del(RedisColumn.orderTime(val));
						connection.sRem(RedisColumn.peopleToOrder(peopleId),
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
		final String time = tool.getKey(RedisColumn.rechargeTime(val));
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
						// 添加人员到充值记录的映射
						connection.sRem(RedisColumn.peopleToRecharge(people),
								(val + "").getBytes());
						connection.sRem(RedisColumn.timeToRecharge(time),
								(val + "").getBytes());
						connection.sRem(RedisColumn.rechargeList(),
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
						connection.sAdd(RedisColumn.menuList(),
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
		Set<byte[]> ll = tool.getSet(RedisColumn.menuList());
		List<Menu> result = new ArrayList<Menu>();
		for (Object o : ll) {
			int key = Integer.parseInt(new String((byte[]) o));
			Menu p = new Menu();
			p.setMenuName(tool.getKey(RedisColumn.menuName(key)));
			p.setClickTimes((int) tool.getScore(RedisColumn.menuScore(),
					(byte[]) o));
			p.setMenuUrl(tool.getKey(RedisColumn.menuUrl(key)));
			p.setSno(key);
			result.add(p);
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

}
