package redis;

public class Key {
	private String k;
	private final static String SPLIT = ":";

	public Key(String k) {
		this.k = k;
	}

	public Key add(String s) {
		return KeyFactory.getInstance().getKey(this.k += SPLIT + s);
	}

	public Key add(int s) {
		return KeyFactory.getInstance().getKey(this.k += SPLIT + s);
	}

	public byte[] getId() {
		return this.k.getBytes();
	}
}
