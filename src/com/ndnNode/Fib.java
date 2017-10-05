package com.ndnNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ndnPackage.Interest;
import com.ndnPackage.Name;

public class Fib {
	public HashMap<Name,HashSet<Router>> fibTable = new HashMap<Name,HashSet<Router>>();

	
//	public void forwardInterest(Interest interest,Map<Router, Integer> delayTimeMap) throws InterruptedException{
////		Integer delayTime = delayTimeMap.get(router);
//		HashSet routerList = fibTable.get(interest.getName());
//		synchronized(routerList){
//			Iterator<Router> it = routerList.iterator();
//			while(it.hasNext()){
//				Router router = it.next();
//				this.forwardInterest(interest, router ,delayTimeMap.get(router));
//			}
//		}
//	}
//	
//	public void forwardInterest(Interest interest,Router router, Integer delayTime) throws InterruptedException{
//		Thread.sleep(delayTime);
//		router.receiveInterest(interest);
//	}
	public HashSet get(Name name){
		//return this.fibTable.get(name);
		for(Name prefix:this.fibTable.keySet()){
			if(name.getPrefix().startsWith(prefix.getPrefix())){
				return this.fibTable.get(prefix);
			}
		}
		return null;
	}
	
	public HashSet get(Interest interest){
		return this.get(interest.getName());
	}
	
	public void add(Name name,Router router){
		System.out.println("fib add name:"+name.getPrefix());
		if(fibTable.containsKey(name)){
			fibTable.get(name).add(router);
		}else{
			HashSet<Router> routerList= new HashSet<Router>();
			routerList.add(router);
			fibTable.put(name, routerList);
		}
	}
	
	public void add(Name name,HashSet<Router> routerList){
		if(fibTable.containsKey(name)){
			fibTable.get(name).addAll(routerList);
		}else{
			fibTable.put(name, routerList);
		}
	}
	public HashMap<Name, HashSet<Router>> getFibTable() {
		return fibTable;
	}
	
	public void setFibTable(HashMap<Name, HashSet<Router>> fibTable) {
		this.fibTable = fibTable;
	}
}
