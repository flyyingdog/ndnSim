package com.ndnNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ndnPackage.Data;
import com.ndnPackage.Interest;
import com.ndnPackage.Name;

public class Pit {
	private HashMap<Name, HashSet<Router>> pitTable = new HashMap<Name,HashSet<Router>>();
	
	public HashMap<Name, HashSet<Router>> getPitTable() {
		return pitTable;
	}

	public void setPitTable(HashMap<Name, HashSet<Router>> pitTable) {
		this.pitTable = pitTable;
	}
	
	public HashSet<Router> get(Name name){
		return pitTable.get(name);
	}
	
	public HashSet<Router> get(Data data){
		return pitTable.get(data.getName());
	}
	
	public HashSet<Router> get(Interest interest){
		return this.get(interest.getName());
	}
	
	public void remove(Name name){
		if(this.pitTable.containsKey(name)){
			this.pitTable.remove(name);
			System.out.println("remove pitTicket "+ name.getPrefix());
		}else{
			System.out.println("no use data named " + name.getPrefix());
		}
	}
	
	public void remove(Data data){
		System.out.println("pit remove Data "+data.getName().getPrefix());
		this.remove(data.getName());
	}
	
	public void add(Name name,Router router){
		if(pitTable.containsKey(name)){
			pitTable.get(name).add(router);
		}else{
			HashSet<Router> routerList= new HashSet<Router>();
			routerList.add(router);
			pitTable.put(name,  routerList);
			
		}
	}
	
	public void add(Interest interest,Router router){
		this.add(interest.getName(), router);
	}
	
	public void add(Name name,HashSet<Router> routerList){
		if(pitTable.containsKey(name)){
			pitTable.get(name).addAll(routerList);
		}else{
			pitTable.put(name, routerList);
		}
	}
}
