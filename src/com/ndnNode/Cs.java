package com.ndnNode;
import com.ndnPackage.Data;
import com.ndnPackage.Interest;
import com.ndnPackage.Name;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Cs {
	public Map<Name,Data> dataMap = new TreeMap<Name,Data>();
	private int delayTime;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataMap == null) ? 0 : dataMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cs other = (Cs) obj;
		if (dataMap == null) {
			if (other.dataMap != null)
				return false;
		} else if (!dataMap.equals(other.dataMap))
			return false;
		return true;
	}
	
	public void add(Name name,Data data){
		dataMap.put(name, data);
	}
	
	public boolean hasData(Name name){
		if(name.getUrlName()!=null && name.getDataName()!=null){
			Name dataName = new Name(name.getDataName());
			return this.dataMap.containsKey(dataName);
		}else{
			return this.dataMap.containsKey(name);
		}
	}
	
	public void add(Data data){
		dataMap.put(data.getName(), data);
	}
	
	public Data get(Name name){
		if(name.getDataName()!=null && name.getUrlName()!=null){
			name = new Name(name.getDataName());
		}
		if (dataMap.containsKey(name)){
			return dataMap.get(name);
		}else{
			return null;
		}
	}
	
	public Data get(Interest interest){
		return this.get(interest.getName());
	}
	
	public boolean match(Interest interest){
		if(dataMap.containsKey(interest.getName())){
			return true;
		}else{
			return false;
		}
	}
	
	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	
	public Data getLastDataWithPrefix(String prefix){
		int num = 0;
		Data data=null;
		for(Name name:this.getDataMap().keySet()){
			String namePrefix = name.getPrefix();
			if(namePrefix.contains(prefix)){
				data = data==null?this.getDataMap().get(name):data;
				String suffix = namePrefix.split("/")[namePrefix.split("/").length-1];
				if(Character.isDigit(suffix.charAt(0))){
					//doSomething:generateNewDataRequest
					if(Integer.parseInt(suffix)>num){
						data = this.getDataMap().get(name);
						num = Integer.parseInt(suffix);
					}
				}
			}
		}
		return data;
	}
	
	public Data getLastDataWithPrefix(Name name){
		Data data = this.getLastDataWithPrefix(name.getPrefix());
		return data;
	}
	
//	public String getCsDigest(){
//		MessageDigest messageDigest;
//		StringBuffer allCsName=new StringBuffer();
//		String encodeStr="";
//		for(Name name:dataMap.keySet()){
//			allCsName.append(name.getPrefix());
//		}
//		try {
//			messageDigest = MessageDigest.getInstance("SHA-256");
//			messageDigest.update(allCsName.toString().getBytes("UTF-8"));
//			encodeStr = this.byte2Hex(messageDigest.digest());
//			
//		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
////		System.out.println("generate cs digest "+encodeStr);
//		return encodeStr;
//	}
	
	 /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
	
	public Map<Name, Data> getDataMap() {
		return dataMap;
	}
	public void setDataMap(HashMap<Name, Data> dataMap) {
		this.dataMap = dataMap;
	}
}
