package redis;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

/**
 * reids管理小工具.
 * 
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
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

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

	public String deleteKey() {
		redisTool.deleteKey(keys);
		return null;
	}

	public String rename() {
		redisTool.rename(keys, value);
		return null;
	}

	public String removeListValue() {
		redisTool.removeListValue(keys, value);
		return null;
	}

	public String removeSetValue() {
		redisTool.removeSetValue(keys, value);
		return null;
	}

	public String removeZScore() {
		redisTool.removeZScore(keys, value);
		return null;
	}

	public String removeHashValue() {
		redisTool.removeHashValue(keys, value);
		return null;
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
	 * 
	 * 判断是否存在一个指定的key.
	 * 
	 * @return
	 */
	public String exists() {
		if (exists != null) {
			if (redisTool.existsKey(exists.getBytes()))
				write("" + redisTool.getJsonData(exists.getBytes()));
			else
				write("{'type':'none'}");
		} else
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

	private String keytype;
	private String val1;
	private String newKeyName;

	public String getNewKeyName() {
		return newKeyName;
	}

	public void setNewKeyName(String newKeyName) {
		this.newKeyName = newKeyName;
	}

	public String getKeytype() {
		return keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public String getVal1() {
		return val1;
	}

	public void setVal1(String val1) {
		this.val1 = val1;
	}

	public String getVal2() {
		return val2;
	}

	public void setVal2(String val2) {
		this.val2 = val2;
	}

	private String val2;

	public String addKey() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=GBK");
		try {
			redisTool.addKey(keytype, newKeyName, val1, val2);
			response.getWriter().print("添加值到" + newKeyName + "类型成功."); 
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().print("添加值到" + newKeyName + "类型失败.");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
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
		if (configs != null && configs.size() > 0)
			build = build.deleteCharAt(build.lastIndexOf(","));
		build.append("]");
		write(build.toString());
		return null;
	}
}
