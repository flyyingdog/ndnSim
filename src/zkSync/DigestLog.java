package zkSync;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.utils.*;
public class DigestLog {
	public Map<String,LinkedList<String>> digestLog = new HashMap<String,LinkedList<String>>();

	public void add(String prefix){
		String group = StringUtil.trimFirstAndLastChar(prefix, '/').split("/")[0];

		if(!digestLog.containsKey(group)){
			digestLog.put(group, new LinkedList<String>());
		}
		digestLog.get(group).add(prefix);

	}
	
	public List<String> get(String idx){
		List<String> dataList = new LinkedList<String>();
		String digest = StringUtil.trimFirstAndLastChar(idx, '/');
		String group = digest.split("@")[0];
		String index = digest.split("@")[1];
		int count = 0;
		boolean flag = false;
		LinkedList<String> log = digestLog.get(group);
		if(log==null) return dataList;
		for(String data:log){
			if(Integer.valueOf(index)==count){
				flag = true;
			}
			if(flag){
				dataList.add(data);
			}else count++;
		}
		return dataList;
	}
	//获取当前最新的digest
	public String length(String prefix){
		String group;
		if(prefix.startsWith("/")){
			group = prefix.split("/")[1];			
		}else{
			group = prefix.split("/")[0];
		}
		LinkedList<String> groupLog = digestLog.get(group);
		String result = "";
		if(groupLog==null){
			return null;
		}else{
			String index = String.valueOf((groupLog.size()-1));
			return group+"@"+index;
		}
	}
}

