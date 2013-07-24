package redis;

/**
 * 核心的键注册中心.
 * 
 * @author lisq
 * 
 */
public class RegisterSystem {
	public final static String REGISTER_PRE = "_regiest_system";
	public final static String REGISTER_SYSTEM = "_system";
	public final static String REGISTER_TABLE = "_table"; 
	public final static String REGISTER_COLUMN = "_column";
	public final static String REGISTER_ARG = "_arg";
	public final static String REGISTER_FORMATTER = "_formater";

	public static byte[] system(String system) {
		return new Key(REGISTER_PRE).add(system).getId();
	}

	public static byte[] table(String system, String table) {
		return new Key(REGISTER_PRE).add(system).add(table).getId();
	}

	public static byte[] column(String system, String table, String column) {
		return new Key(REGISTER_PRE).add(system).add(table).add(column).getId();
	} 
}
