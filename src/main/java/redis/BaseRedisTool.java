package redis;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

/**
 * 基础的redis操作封装. 只提供关于只读方法的封装，对于写操作可能涉及到事务等操作,示例代码：DictionaryRedis.java
 * 
 * @author lisq
 * 
 */

@SuppressWarnings("unchecked")
public class BaseRedisTool {
	private RedisTemplate<Serializable, Serializable> template;

	/**
	 * 返回redis模板.
	 * 
	 * @return
	 */
	public RedisTemplate<Serializable, Serializable> getTemplate() {
		return template;
	}

	/**
	 * 注入redis模板.
	 * 
	 * @param template
	 */
	public void setTemplate(RedisTemplate<Serializable, Serializable> template) {
		this.template = template;
	}

	/**
	 * 返回list
	 * 
	 * @param k
	 * @return
	 */
	public List<byte[]> getList(final byte[] k) {
		return template.execute(new RedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.lRange(k, 0, -1);
			}
		});
	}

	/**
	 * 按照顺序返回有权重的list.
	 * 
	 * @param k
	 * @return
	 */
	public Set<Tuple> getListWithScore(final byte[] k) {
		return template.execute(new RedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Set<Tuple> r = connection.zRevRangeWithScores(k, 0, -1);
				return r;
			}
		});
	}

	/**
	 * 返回指定排名的元素
	 * 
	 * @param k
	 * @param top
	 * @return
	 */
	public Set<Tuple> getListWithScore(final byte[] k, final int top) {
		return template.execute(new RedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Set<Tuple> r = connection.zRevRangeWithScores(k, 0, top);
				return r;
			}
		});
	}

	/**
	 * 返回一个map对应的全部的值.
	 * 
	 * @param k
	 * @return
	 */
	public Map<byte[], byte[]> getMapAll(final byte[] k) {
		return template.execute(new RedisCallback<Map<byte[], byte[]>>() {
			@Override
			public Map<byte[], byte[]> doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.hGetAll(k);
			}
		});
	}

	/**
	 * 返回键值对应的值.
	 * 
	 * @param k
	 * @return
	 */
	public Set<byte[]> getSet(final byte[] k) {
		return template.execute(new RedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.sMembers(k);
			}
		});
	}

	/**
	 * 返回一个键对应的字符串缓存值.
	 */
	public String getKey(final byte[] k) {
		byte[] b = template.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.get(k);
			}
		});
		if (b != null)
			return new String(b);
		return null;
	}

	/**
	 * 返回指定的zscore分数.
	 * 
	 * @param k
	 * @param item
	 * @return
	 */
	public double getScore(final byte[] k, final byte[] item) {
		Double result = template.execute(new RedisCallback<Double>() {
			@Override
			public Double doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.zScore(k, item);
			}
		});
		if (result == null)
			return 0;
		return result;
	}

	/**
	 * 返回list的长度.
	 * 
	 * @param k
	 * @return
	 */
	public Long getLen(final byte[] k) {
		return template.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.lLen(k);
			}
		});
	}

	/**
	 * 判断一个key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean existsKey(final byte[] key) {
		return template.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.exists(key);
			}

		});
	}

	/**
	 * 返回一个hash对应键的值.
	 * 
	 * @param h
	 * @param key
	 * @return
	 */
	public byte[] getHashByKey(final byte[] h, final byte[] key) {
		return template.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.hGet(h, key);
			}

		});
	}

	/**
	 * 生成主键.
	 * 
	 * @param key
	 * @return
	 */
	public int generateKey(final byte[] key) {
		Long result = template.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.incr(key);
			}
		});
		return result.intValue();
	}

	/**
	 * 返回一个键对应的值,包含各种类型.
	 * 
	 * @param key
	 * @return
	 */
	public Object getVal(final byte[] key) {
		return template.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				DataType tp = connection.type(key);
				if (DataType.STRING.equals(tp)) {
					return new String(connection.get(key));
				} else if (DataType.HASH.equals(tp)) {
					return connection.hGetAll(key);
				} else if (DataType.LIST.equals(tp)) {
					return connection.lRange(key, 0, -1);
				} else if (DataType.SET.equals(tp)) {
					return connection.sMembers(key);
				} else if (DataType.ZSET.equals(tp)) {
					return connection.zRangeWithScores(key, 0, -1);
				} else
					return "";
			}

		});
	}

	/**
	 * 返回一个键对应的json串形式.
	 * 
	 * @param key
	 * @return
	 */
	public String getJsonData(final byte[] key) {
		return template.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				DataType tp = connection.type(key);
				if (DataType.STRING.equals(tp)) {
					return "{'type':'string','value':'"
							+ new String(connection.get(key)) + "'}";
				} else if (DataType.HASH.equals(tp)) {
					Map<byte[], byte[]> m = connection.hGetAll(key);
					Iterator<Entry<byte[], byte[]>> it = m.entrySet()
							.iterator();
					StringBuilder bui = new StringBuilder(100);
					bui.append("[");
					while (it.hasNext()) {
						Entry<byte[], byte[]> e = it.next();
						String key = new String(e.getKey());
						String val = new String(e.getValue());
						bui.append("{'key':'" + key + "','value':'" + val
								+ "'},");
					}
					if (m != null && m.size() > 0) {
						bui = bui.deleteCharAt(bui.lastIndexOf(","));
					}
					return "{'type':'hash','value':"
							+ bui.append("]").toString() + "}";
				} else if (DataType.LIST.equals(tp)) {
					List<byte[]> list = connection.lRange(key, 0, -1);
					StringBuilder bui = new StringBuilder(100);
					bui.append("[");
					for (byte[] b : list) {
						bui.append("'" + new String(b) + "',");
					}
					if (list != null && list.size() > 0) {
						bui = bui.deleteCharAt(bui.lastIndexOf(","));
					}
					return "{'type':'list','value':"
							+ bui.append("]").toString() + "}";
				} else if (DataType.SET.equals(tp)) {
					Set<byte[]> s = connection.sMembers(key);
					StringBuilder bui = new StringBuilder(100);
					bui.append("[");
					for (byte[] b : s) {
						bui.append("'" + new String(b) + "',");
					}
					if (s != null && s.size() > 0) {
						bui = bui.deleteCharAt(bui.lastIndexOf(","));
					}
					return "{'type':'set','value':"
							+ bui.append("]").toString() + "}";
				} else if (DataType.ZSET.equals(tp)) {
					Set<Tuple> s = connection.zRangeWithScores(key, 0, -1);
					StringBuilder bui = new StringBuilder(100);
					bui.append("[");
					for (Tuple b : s) {
						bui.append("{'value':'" + new String(b.getValue())
								+ "','score':'" + b.getScore() + "'},");
					}
					if (s != null && s.size() > 0) {
						bui = bui.deleteCharAt(bui.lastIndexOf(","));
					}
					return "{'type':'zset','value':"
							+ bui.append("]").toString() + "}";
				} else
					return "";
			}

		});
	}

	/**
	 * 返回匹配的全部key的集合.
	 * 
	 * @param key
	 * @return
	 */
	public Set<byte[]> allKeys(final byte[] key) {
		return template.execute(new RedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.keys(key);
			}
		});
	}

	private byte[] strToByte(String s) {
		return s.getBytes();
	}

	public void addKey(String keytype, String newKeyName, String val1,
			String val2) {
		final byte[] k = strToByte(keytype);
		final String str2 = val2;
		final byte[] n = strToByte(newKeyName);
		final byte[] v1 = strToByte(val1);
		final byte[] v2 = strToByte(val2);
		if ("str".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.set(n, v1);
					return null;
				}
			});
		} else if ("strnx".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.setNX(n, v1);
					return null;
				}
			});
		} else if ("listL".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.lPush(n, v1);
					return null;
				}
			});
		} else if ("listR".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.rPush(n, v1);
					return null;
				}
			});
		} else if ("set".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.sAdd(n, v1);
					return null;
				}
			});
		} else if ("zset".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.zAdd(n, Double.parseDouble(str2), v1);
					return null;
				}
			});
		} else if ("hash".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.hSet(n, v1, v2);
					return null;
				}
			});
		} else if ("hashnx".equals(keytype)) {
			template.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.hSetNX(n, v1, v2);
					return null;
				}
			});
		}

	}

	/**
	 * 返回数据库的大小.
	 * 
	 * @return
	 */
	public Long dbSize() {
		return template.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * 返回指定的配置信息.
	 * 
	 * @return
	 */
	public List<String> getConfig(final String str) {
		return template.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.getConfig(str);
			}
		});
	}

	/**
	 * 删除匹配的全部对应的key.
	 * 
	 * @param key
	 */
	public void deletePreKeys(String key) {
		if (key == null)
			return;
		final Set<byte[]> allKeys = allKeys(key.getBytes());
		@SuppressWarnings("rawtypes")
		SessionCallback sessionCallback = new SessionCallback() {
			@Override
			public Object execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						Iterator<byte[]> it = allKeys.iterator();
						while (it.hasNext()) {
							byte[] key = it.next();
							if (key != null) {
								connection.del(key);
							}
						}
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		template.execute(sessionCallback);
	}

	public void deleteKey(String key) {
		if (key == null)
			return;
		final String keykey = key;

		template.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.del(keykey.getBytes());
				return null;
			}

		});
	}

	/**
	 * 删除list里面的一个value.
	 * 
	 * @param key
	 * @param value
	 */
	public void removeListValue(String key, String value) {
		if (key == null || value == null)
			return;
		final String keykey = key;
		final String va = value;

		template.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.lRem(keykey.getBytes(), 0, va.getBytes());
				return null;
			}

		});
	}

	public void rename(String key, String value) {
		if (key == null || value == null)
			return;
		final String keykey = key;
		final String va = value;

		template.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.rename(keykey.getBytes(), va.getBytes());
				return null;
			}

		});
	}

	/**
	 * 删除set里面的值.
	 * 
	 * @param key
	 * @param value
	 */
	public void removeSetValue(String key, String value) {
		if (key == null || value == null)
			return;
		final String keykey = key;
		final String va = value;

		template.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.sRem(keykey.getBytes(), va.getBytes());
				return null;
			}

		});
	}

	/**
	 * 删除hash里面的值.
	 * 
	 * @param key
	 * @param value
	 */
	public void removeHashValue(String key, String value) {
		if (key == null || value == null)
			return;
		final String keykey = key;
		final String va = value;

		template.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.hDel(keykey.getBytes(), va.getBytes());
				return null;
			}

		});
	}

	/**
	 * 删除指定的权重的数值.
	 * 
	 * @param key
	 * @param value
	 */
	public void removeZScore(String key, String value) {
		if (key == null || value == null)
			return;
		final String keykey = key;
		final String va = value;

		template.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.zRem(keykey.getBytes(), va.getBytes());
				return null;
			}

		});
	}

	private boolean isEmpty(String str) {
		return str == null || "".equals(str.trim())
				|| "null".equals(str.trim());
	}

	public int regiest(final String system, final String desc) {
		final int val = generateKey(RedisColumn.snoFactory());
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {

					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set((val + "").getBytes(), system.getBytes());
						connection.set(RegisterSystem.desc(val),
								desc.getBytes());
						connection.lPush(RegisterSystem.regiest(),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		getTemplate().execute(sessionCallback);
		return val;
	}

	public int regiest(final String system, final String tableName,
			final String desc) {
		final int val = generateKey(RedisColumn.snoFactory());
		final int systemId = Integer.parseInt(system);
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set((val + "").getBytes(),
								tableName.getBytes());
						connection.set(RegisterSystem.desc(val),
								desc.getBytes());
						connection.lPush(RegisterSystem.system(systemId),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		getTemplate().execute(sessionCallback);
		return val;

	}

	public int regiest(final String system, final String tableName,
			final String column, final String desc) {
		final int val = generateKey(RedisColumn.snoFactory());

		final int systemId = Integer.parseInt(system);
		final int tableId = Integer.parseInt(tableName);
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set((val + "").getBytes(), column.getBytes());
						connection.set(RegisterSystem.desc(val),
								desc.getBytes());
						connection.lPush(
								RegisterSystem.table(systemId, tableId),
								(val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		getTemplate().execute(sessionCallback);
		return val;
	}

	public int regiest(final String system, final String tableName,
			final String column, final String formater, final String desc) {
		final int val = generateKey(RedisColumn.snoFactory());

		final int systemId = Integer.parseInt(system);
		final int tableId = Integer.parseInt(tableName);
		final int columnId = Integer.parseInt(column);
		SessionCallback<Integer> sessionCallback = new SessionCallback<Integer>() {
			@Override
			public Integer execute(RedisOperations operations)
					throws DataAccessException {
				operations.multi();
				operations.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						connection.set((val + "").getBytes(),
								formater.getBytes());
						connection.set(RegisterSystem.desc(val),
								desc.getBytes());
						connection.lPush(RegisterSystem.column(systemId,
								tableId, columnId), (val + "").getBytes());
						return null;
					}

				});
				operations.exec();
				return null;
			}
		};
		getTemplate().execute(sessionCallback);
		return val; 
	}
}
