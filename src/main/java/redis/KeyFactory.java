package redis;

import java.util.HashMap;
import java.util.Map;

public class KeyFactory {
	Map<String, Key> pool = new HashMap<String, Key>();

	private KeyFactory() {

	}

	private static class SingletonHolder {
		private static KeyFactory instance = new KeyFactory();
	}

	public static KeyFactory getInstance() {
		return SingletonHolder.instance;
	}

	public Key getKey(String key) {
		Key result = pool.get(key);
		if (result == null) {
			result = new Key(key);
			pool.put(key, result);
		}
		return result;
	}

	public int getKeySize() {
		return pool.size();
	}
}
