package redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import bean.Menu;

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
	private String systemId;
	private String tableId;
	private String columnId;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

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

	private List<Menu> getMenus(byte[] by) {
		List<Menu> all_system = new ArrayList<Menu>();
		List<byte[]> allSystem = redisTool.getList(by);
		for (byte[] b : allSystem) {
			int sno = Integer.parseInt(new String(b)); 
			Menu m = new Menu();
			m.setMenuUrl(redisTool.getKey(RegisterSystem.code(sno)));
			m.setSno(sno);
			m.setMenuName(redisTool.getKey(RegisterSystem.desc(sno)));
			all_system.add(m);
		}
		return all_system;
	}

	private List<Menu> getAllTable(int systemId) {
		return getMenus(RegisterSystem.system(systemId));
	}

	private List<Menu> getAllColumn(int systemId, int tableId) {
		return getMenus(RegisterSystem.table(systemId, tableId));
	}

	private List<Menu> getAllSystem() {
		return getMenus(RegisterSystem.regiest());
	}

	private String menuListToStr(List<Menu> mns) {
		StringBuilder bui = new StringBuilder(100);
		if (mns != null && mns.size() > 0)
			for (Menu m : mns) {
				bui.append("<option value='" + m.getSno() + "' code='"+m.getMenuUrl()+"'>"
						+ m.getMenuName() + "</option>");
			}
		return bui.toString();
	}

	public String getColumnByTable() {
		List<Menu> allSystem = getAllColumn(Integer.parseInt(systemId),
				Integer.parseInt(tableId));
		write(menuListToStr(allSystem));
		return null;
	}

	public String getTableBySystem() {
		List<Menu> allTable = getAllTable(Integer.parseInt(systemId));
		write(menuListToStr(allTable));
		return null;
	}

	public String manager() {
		List<Menu> allSystem = getAllSystem();
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("count", redisTool.dbSize());
		request.setAttribute("allSystem", allSystem);
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

	private String system;
	private String columnName;
	private String tbname;
	private String formater;

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTbname() {
		return tbname;
	}

	public void setTbname(String tbname) {
		this.tbname = tbname;
	}

	public String getFormater() {
		return formater;
	}

	public void setFormater(String formater) {
		this.formater = formater;
	}

	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String regiestSystem() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=GBK");
		try {
			int code = redisTool.regiest(system, desc);
			response.getWriter().print("{'result':1,'code':'" + code + "'}");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().print("{'result':0}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public String regiestTable() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=GBK");
		try {
			int code = redisTool.regiest(system, tbname, desc);
			response.getWriter().print("{'result':1,'code':'" + code + "'}");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().print("{'result':0}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public String regiestColumn() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=GBK");
		try {
			int code = redisTool.regiest(system, tbname, columnName, desc);
			response.getWriter().print("{'result':1,'code':'" + code + "'}");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().print("{'result':0}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public String regiestFormat() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=GBK");
		try {
			int code = redisTool.regiest(system, tbname, columnName, formater,
					desc);
			response.getWriter().print("{'result':1,'code':'" + code + "'}");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().print("{'result':0}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

}
