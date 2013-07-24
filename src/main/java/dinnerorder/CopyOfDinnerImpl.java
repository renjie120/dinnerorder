package dinnerorder;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import redis.RedisColumn;

/**
 * 进行订单的详情记录操作.
 * 
 * @author lsq
 * 
 */

@SuppressWarnings("unchecked")
public class CopyOfDinnerImpl implements RedisCallback {
	int g;
	String t;
	String m;
	List<String> orders;

	public CopyOfDinnerImpl(int g, String t, String m, List<String> orders) {
		this.g = g;
		this.t = t;
		this.m = m;
		this.orders = orders;
	}

	@Override
	public Object doInRedis(RedisConnection connection)
			throws DataAccessException {
		connection.set(RedisColumn.timeAndGroupToAvg(t, g), m.getBytes());
		// 设置全部的当天的消费值.
		for (String o : orders) { 
			int _o = Integer.parseInt(o); 
			System.out.println(connection.get(RedisColumn.orderSingle(_o)));
			System.out.println(new String(RedisColumn.orderSingle(_o)));
			// 如果不是单点，该订单的消费额就是平均值.
			if ("0".equals(new String(connection.get(RedisColumn.orderSingle(_o)))))
				connection.set(RedisColumn.orderCost(_o), m.getBytes());
			else {
				// 单点的话消费额就是该订单的金额，而不是平均值.
				connection.set(RedisColumn.orderCost(_o),
						connection.get(RedisColumn.orderMoney(_o)));
			}
		}
		return null;
	}

}
