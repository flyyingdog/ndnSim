package com.ndnNode;

import java.util.HashSet;
import java.util.Iterator;

import com.ndnPackage.Data;

public class Producer extends Router implements Runnable {
	public HashSet<Data> responseList = new HashSet<Data>();
	
	public Producer(int address,HashSet<Data> responseList){
		super(address);
		this.responseList = responseList;
		Iterator<Data> it = responseList.iterator();
		while(it.hasNext()){
			Data data = it.next();
			this.getCs().add(data.getName(),data);
		}
	}
	
	
	
	public HashSet<Data> getResponseList() {
		return responseList;
	}
	public void setResponseList(HashSet<Data> responseList) {
		this.responseList = responseList;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
	
}
