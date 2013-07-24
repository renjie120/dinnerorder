package redis;

/**
 * 核心的键注册中心.
 * 
 * @author lisq
 * 
 */
public class RegisterSystem {
	public final static String REGISTER_PRE = "_regiest";
	public final static String REGISTER_SYSTEM = "_system";
	public final static String REGISTER_TABLE = "_table";
	public final static String REGISTER_COLUMN = "_column";
	public final static String REGISTER_FORMATTER = "_formater";
	public final static String REGISTER_DESC = "_desc";

	public static byte[] regiest() {
		return new Key(REGISTER_PRE).getId();
	}

	public static byte[] system(int system) {
		return new Key(REGISTER_PRE).add(REGISTER_SYSTEM).add(system).getId();
	}

	public static byte[] desc(int code) {
		return new Key(REGISTER_PRE).add(REGISTER_DESC).add(code).getId();
	}
	
	public static byte[] table(int system, int table) {
		return new Key(REGISTER_PRE).add(REGISTER_SYSTEM).add(system)
				.add(REGISTER_TABLE).add(table).getId();
	}

	public static byte[] column(int system, int table, int column) {
		return new Key(REGISTER_PRE).add(REGISTER_SYSTEM).add(system)
				.add(REGISTER_TABLE).add(table).add(REGISTER_COLUMN)
				.add(column).getId();
	}

}
