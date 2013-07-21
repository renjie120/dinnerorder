package dinnerorder;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import redis.RedisColumn;
import bean.Order;

public class Callback implements  RedisCallback{
	public  Order p ;
	public  int peopleId ;
	public  int val ;
	public Callback(Order p,int val,int peopleId){
		this.p = p;
		this.val =val;
		this.peopleId = peopleId;
	}
	@Override
	public Object doInRedis(RedisConnection connection) throws DataAccessException {
		connection.set(RedisColumn.orderDinner(val),
				(p.getDinner()).getBytes());
		connection.set(RedisColumn.orderMoney(val),
				(p.getMoney()).getBytes());
		connection.set(RedisColumn.orderPeople(val),
				(peopleId + "").getBytes());
		//每添加一个菜单，就添加一个权重.
		connection.zIncrBy(RedisColumn.orderPeopleWithScore(), 1, (peopleId + "").getBytes()); 
		connection.set(RedisColumn.orderSingle(val),
				(p.getIsSingle()).getBytes());
		connection.set(RedisColumn.orderTime(val),
				(p.getTime()).getBytes()); 
		connection.sAdd(RedisColumn.peopleToOrder(peopleId),
				(val + "").getBytes());
		connection.sAdd(RedisColumn.timeToOrder(p.getTime()),
				(val + "").getBytes());
		connection.lPush(RedisColumn.timeToMoney(p.getTime()),
				(p.getMoney()).getBytes());
		connection.sAdd(RedisColumn.orderList(),
				(val + "").getBytes());
		connection.sAdd(RedisColumn.orderTimeSet(),
				(p.getTime()).getBytes());
		//保存在一个交集中.
//		connection.sInterStore(RedisColumn.peopleAndTimeToOrder(p.getPeopleSno(), p.getTime()),
//				RedisColumn.peopleToOrder(p.getPeopleSno()),
//				RedisColumn.timeToOrder(p.getTime()));
		return null;
	}
	 
}
