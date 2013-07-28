package redis;

/**
 * 用于生成redis的key的工具类.
 * @author lisq
 *
 */
public class Key {
	private String k;
	private final static String SPLIT = ":";

	public Key(String k) {
		this.k = k;
	}

	/**
	 * 添加一个节点用分隔符分开.
	 * @param s
	 * @return
	 */
	public Key add(String s) { 
		this.k+=SPLIT + s;
		//return KeyFactory.getInstance().getKey(k);
		return this;
	}

	/**
	 * 添加一个节点用分隔符分开.
	 * @param s
	 * @return
	 */
	public Key add(int s) {
		this.k+=SPLIT + s;
		return this;
		//return KeyFactory.getInstance().getKey(k + SPLIT + s);
	}

	/**
	 * 返回键的字节数组形式.
	 * @return
	 */
	public byte[] getId() {
		return this.k.getBytes();
	}
}
