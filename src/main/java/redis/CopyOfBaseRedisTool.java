package redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

/**
 * 基础的redis操作封装. 只提供关于只读方法的封装，对于写操作可能涉及到事务等操作,示例代码：DictionaryRedis.java
 * 
 * @author lisq
 * 
 */

@SuppressWarnings("unchecked")
public class CopyOfBaseRedisTool implements RedisCallback {
	int val;
	String system;
	String desc;

	public CopyOfBaseRedisTool(int val, String system, String desc) {
		this.val = val;
		this.system = system;
		this.desc = desc;
	}

	@Override
	public Object doInRedis(RedisConnection connection)
			throws DataAccessException {
		connection.set((val + "").getBytes(), system.getBytes());
		connection.set(RegisterSystem.desc(val), desc.getBytes());
		connection.lPush(RegisterSystem.regiest(), (val + "").getBytes());
		return null;
	}

}
