package redis;

public class Key {
	private String k;
	private final static String SPLIT = ":";

	public Key(String k) {
		this.k = k;
	}

	public Key add(String s) { 
		this.k+=SPLIT + s;
		//return KeyFactory.getInstance().getKey(k);
		return this;
	}

	public Key add(int s) {
		this.k+=SPLIT + s;
		return this;
		//return KeyFactory.getInstance().getKey(k + SPLIT + s);
	}

	public byte[] getId() {
		return this.k.getBytes();
	}
}
