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
		if (order.getPeopleSno() == -1) {
			People people = new People();
			people.setName(order.getPeopleName());
			peopleId = savePeople(people);
		} else {
			peopleId = order.getPeopleSno();
		}
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new Callback(p, val, peopleId)); 
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
						connection.lPush(
								RedisColumn.peopleToRecharge(p.getPeopleSno()),
								(val + "").getBytes());
						connection.lPush(
								RedisColumn.timeToRecharge(p.getTime()),
								(val + "").getBytes());
						// 添加人员的金额到金额总数中.
						connection.lPush(RedisColumn.peopleToMoney(p
								.getPeopleSno()), p.getMoney().getBytes());
						connection.lPush(RedisColumn.rechargeList(),
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
	public void deleteRecharge(ReCharge recharge) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ReCharge> getRecharges() {
		List ll = tool.getList(RedisColumn.rechargeList());
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
	public void deletePeople(People order) {
		// TODO Auto-generated method stub

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
	public void deleteOrder(Order order) {
		// TODO Auto-generated method stub

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
		//添加一个焦急
		tool.getTemplate().execute(new RedisCallback() { 
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				// 保存在一个交集中.
				connection.sInterStore(
						RedisColumn.peopleAndTimeToOrder(
								p, t),
						RedisColumn.peopleToOrder(p),
						RedisColumn.timeToOrder(t));
				return null;
			}

		});
		
		Set<byte[]> s = tool.getSet(RedisColumn.peopleAndTimeToOrder(p, t));
		System.out.println("查找时间+人员："+new String(RedisColumn.peopleAndTimeToOrder(p, t)));
		List<String> result = new ArrayList<String>();
		Iterator<byte[]> it = s.iterator();
		while (it.hasNext()) {
			result.add(new String(it.next()));
		} 
		return result;
	}

}
