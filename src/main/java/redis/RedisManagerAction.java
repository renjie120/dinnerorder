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
	/**
	 * 查询界面的配置信息
	 */
	private String config;
	/**
	 * 进行模糊匹配的键
	 */
	private String keys;
	/**
	 * 输入的键值
	 */
	private String value;
	/**
	 * 系统id
	 */
	private String systemId;
	/**
	 * 表名id
	 */
	private String tableId;
	/**
	 * 列id
	 */
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

	/**
	 * 得到一个系统注册的信息的实体类.
	 * @param by
	 * @return
	 */
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

	/**
	 * 返回全部的注册的表.
	 * @param systemId
	 * @return
	 */
	private List<Menu> getAllTable(int systemId) {
		return getMenus(RegisterSystem.system(systemId));
	}

	/**
	 * 返回全部注册的列.
	 * @param systemId
	 * @param tableId
	 * @return
	 */
	private List<Menu> getAllColumn(int systemId, int tableId) {
		return getMenus(RegisterSystem.table(systemId, tableId));
	}

	/**
	 * 返回全部注册的系统.
	 * @return
	 */
	private List<Menu> getAllSystem() {
		return getMenus(RegisterSystem.regiest());
	}

	/**
	 * 返回菜单列表的table表格字符串.
	 * @param mns
	 * @return
	 */
	private String menuListToStr(List<Menu> mns) {
		StringBuilder bui = new StringBuilder(100);
		if (mns != null && mns.size() > 0)
			for (Menu m : mns) {
				bui.append("<option value='" + m.getSno() + "' code='"+m.getMenuUrl()+"'>"
						+ m.getMenuName() + "</option>");
			}
		return bui.toString();
	}

	/**
	 * 根据表名返回对应的列的列表.
	 * @return
	 */
	public String getColumnByTable() {
		List<Menu> allSystem = getAllColumn(Integer.parseInt(systemId),
				Integer.parseInt(tableId));
		write(menuListToStr(allSystem));
		return null;
	}

	/**
	 * 根据系统返回对应的表名列表.
	 * @return
	 */
	public String getTableBySystem() {
		List<Menu> allTable = getAllTable(Integer.parseInt(systemId));
		write(menuListToStr(allTable));
		return null;
	}

	/**
	 * 初始化界面.
	 * @return
	 */
	public String manager() {
		//查询注册系统的下拉菜单.
		List<Menu> allSystem = getAllSystem();
		HttpServletRequest request = ServletActionContext.getRequest();
		//得到全部的key的长度.
		request.setAttribute("count", redisTool.dbSize());		
		request.setAttribute("allSystem", allSystem);
		return "manager";
	}
	
	/**
	 * 返回表格树第一级别.
	 * @return
	 */
	public String getFirstLevel() {
		List<Menu> allSystem = getAllSystem();
		StringBuilder build = new StringBuilder(100);
		build.append("[");
		for (Menu m : allSystem) {
			build.append("{'sno':'" + m.getSno()).append("',");
			build.append("'code':'" + m.getMenuUrl()).append("',");
			build.append("'parentSno':'-1',");
			build.append("'isLeaf':'0',");
			build.append("'name':'" + m.getMenuName()).append("'},");
		} 
		build = build.deleteCharAt(build.lastIndexOf(","));
		build.append("]");
		write(build.toString());
		return null;
	}



	/**
	 * 删除指定key.
	 * @return
	 */
	public String deleteKey() {
		redisTool.deleteKey(keys);
		return null;
	}
	
	/**
	 * 批量删除key.
	 * @return
	 */
	public String deleteBatchKey() {
		redisTool.deleteBatchKey(keys);
		return null;
	}

	/**
	 * 重命名key.
	 * @return
	 */
	public String rename() {
		redisTool.rename(keys, value);
		return null;
	}

	/**
	 * 删除列表里面的值.
	 * @return
	 */
	public String removeListValue() {
		redisTool.removeListValue(keys, value);
		return null;
	}

	/**
	 * 删除set里面的值
	 * @return
	 */
	public String removeSetValue() {
		redisTool.removeSetValue(keys, value);
		return null;
	}

	/**
	 * 删除含有权重的集合里面的值.
	 * @return
	 */
	public String removeZScore() {
		redisTool.removeZScore(keys, value);
		return null;
	}

	/**
	 * 删除hash表里面的值.
	 * @return
	 */
	public String removeHashValue() {
		redisTool.removeHashValue(keys, value);
		return null;
	}

	/**
	 * 返回指定的配置信息列表.
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

	/**
	 * 向response中写入ajax字符串.
	 * @param str
	 */
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
	 * 键类型.
	 */
	private String keytype;
	/**
	 * 键的值.
	 */
	private String val1;
	/**
	 * 重命名的新的键值.
	 */
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

	/**
	 * 添加新的键.
	 * @return
	 */
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

	/**
	 * 系统名
	 */
	private String system;
	/**
	 * 列名.
	 */
	private String columnName;
	/**
	 * 表名
	 */
	private String tbname;
	/**
	 * 格式化名称.
	 */
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

	/**
	 * 注册系统.
	 * @return
	 */
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
 
	/**
	 * 获取下面下面的表名列表的table字符串..
	 * @return
	 */
	public String openThisSystem(){
		int _systemId = Integer.parseInt(systemId);
		List<Menu> tables = redisTool.findTableBySystem(_systemId);
		StringBuilder bui = new StringBuilder();
		for(Menu m:tables){
			bui.append("<tr><td>&nbsp;&nbsp;<span class=\"open\"  tag=\""+m.getSno()+"\" systemId='"+_systemId+"' onclick=\"openThisTable(this)\">" +
					"</span></td><td>"+m.getMenuName()+"</td><td>"+m.getMenuUrl()+"</td></tr>");
		}
		write(bui.toString());
		return null;
	}
	
	/**
	 * 获取表名下面的列的table字符串.
	 * @return
	 */
	public String openThisTable(){
		int _systemId = Integer.parseInt(systemId);
		int _tableId = Integer.parseInt(tableId);
		List<Menu> tables = redisTool.findColumnByTable(_systemId,_tableId);
		StringBuilder bui = new StringBuilder();
		for(Menu m:tables){
			bui.append("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span class=\"open\"  tag=\""+m.getSno()+"\" systemId='"+_systemId+"' tableId='"+_tableId+"' onclick=\"openThisColumn(this)\">" +
					"</span></td><td>"+m.getMenuName()+"</td><td>"+m.getMenuUrl()+"</td></tr>");
		}
		write(bui.toString());
		return null;
	}
	
	/**
	 * 得到列的下面的格式化的table字符串.
	 * @return
	 */
	public String openThisColumn(){
		int _systemId = Integer.parseInt(systemId);
		int _tableId = Integer.parseInt(tableId);
		int _columnId = Integer.parseInt(columnId);
		List<Menu> tables = redisTool.findFormatterByColumn(_systemId,_tableId,_columnId);
		StringBuilder bui = new StringBuilder();
		for(Menu m:tables){
			bui.append("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<span columnId='"+_columnId+"'  tag=\""+m.getSno()+"\" systemId='"+_systemId+"' tableId='"+_tableId+"' >" +
					"</span></td><td>"+m.getMenuName()+"</td><td>"+m.getMenuUrl()+"</td></tr>");
		}
		write(bui.toString());
		return null;
	}
	
	/**
	 * 注册表名到系统中.
	 * @return
	 */
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

	/**
	 * 注册列到系统中.
	 * @return
	 */
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

	/**
	 * 注册格式化字符串.
	 * @return
	 */
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
