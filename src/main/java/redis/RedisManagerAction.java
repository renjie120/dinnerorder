package redis;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

/**
 * reids管理小工具.
 * @author lsq
 *
 */
public class RedisManagerAction {
	// 序列号
	private static final long serialVersionUID = 1L;
	/**
	 * redis操作工具类.
	 */
	private BaseRedisTool redisTool;
	private String config;
	private String keys;
	private String exists;

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getExists() {
		return exists;
	}

	public void setExists(String exists) {
		this.exists = exists;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public BaseRedisTool getRedisTool() {
		return redisTool;
	}

	public void setRedisTool(BaseRedisTool redisTool) {
		this.redisTool = redisTool;
	}
 
	public String manager() {
		HttpServletRequest request = ServletActionContext.getRequest(); 
		request.setAttribute("count", redisTool.dbSize());
		return "manager";
	}

	/**
	 * 返回全部的控制台信息.
	 * 
	 * @return
	 */
	public String getConfig() {
		List<String> configs = redisTool.getConfig(config);
		StringBuilder build = new StringBuilder(100);
		build.append("[");
		for (String str : configs) {
			build.append("'" + str).append("',");
		}
		if (configs != null && configs.size() > 0)
			build = build.deleteCharAt(build.lastIndexOf(","));
		build.append("]");
		write(build.toString());
		return null;
	}

	/**
	 * 判断是否存在一个指定的key.
	 * 
	 * @return
	 */
	public String exists() {
		if (exists != null){
			if(redisTool.existsKey(exists.getBytes()))
				write("" + redisTool.getJsonData(exists.getBytes()));
			else
				write("不存在键对应的值");
		}
		else
			write("必须输入键！");
		return null;
	}

	public void write(String str) {
		HttpServletResponse response = ServletActionContext.getResponse(); 
		response.setContentType("text/html;charset=GBK");
		try {
			response.getWriter().print(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回指定的全部的keys.
	 * 
	 * @return
	 */
	public String keys() {
		if (keys == null) {
			write("必须输入keys!");
			return null;
		}
		Set<byte[]> configs = redisTool.allKeys(keys.getBytes());
		StringBuilder build = new StringBuilder(100);
		build.append("[");
		for (byte[] str : configs) {
			if (str != null)
				build.append("'" + new String(str)).append("',");
		}
		if(configs!=null&&configs.size()>0)
			build = build.deleteCharAt(build.lastIndexOf(","));
		build.append("]");
		write(build.toString());
		return null;
	}
}
