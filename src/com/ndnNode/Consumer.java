package com.ndnNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.ndnPackage.Data;
import com.ndnPackage.Interest;

import zkSync.DigestLog;
import zkSync.DigestTree;

public class Consumer extends Router implements Runnable{
	public HashSet<Interest> requestList = new HashSet<Interest>(); //用来记录consumer发出的interest，当匹配时从list中消除
	public HashMap<Interest,Integer> isForward = new HashMap<Interest,Integer>();
	public String uniqName;
	
	public String getUniqName() {
		return uniqName;
	}

	public void setUniqName(String uniqName) {
		this.uniqName = uniqName;
	}

	public Consumer() {
		super();
	}

	public Consumer(int address,HashSet<Interest> requestList){
		super(address);
		this.requestList = requestList;
		Iterator<Interest> it  =  requestList.iterator();
		while(it.hasNext()){
			isForward.put(it.next(), 1);
		}
	}
	
	public Consumer(int address,HashSet<Interest> requestList,String uniqName){
		this(address,requestList);
		this.uniqName = uniqName;
	}
	
	@Override
	public void receive(Data data, int srcAddr) throws InterruptedException {
		// TODO Auto-generated method stub
		//this.match(data);
		super.receive(data, srcAddr);
	}

	@Override
	public void forward(Router router, Interest interest){
		// TODO Auto-generated method stub
		super.forward(router, interest);
	}

	public Interest match(Data data) throws CloneNotSupportedException{
		Iterator<Interest> it = requestList.iterator();
		while(it.hasNext()){
			Interest interest = it.next();
			if(interest.getName().equals(data.getName())){
				this.isForward.put(interest, 1);
				this.requestList.remove(interest);
				return interest;
			}
		}
		return null;
	}
	
	public HashSet<Interest> getRequestList() {
		return requestList;
	}


	public void setRequestList(HashSet<Interest> requestList) {
		this.requestList = requestList;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stu
		int flag = 1;
		while(flag==1){
			try {
				Thread.sleep(2000);
				Iterator<Interest> it = this.requestList.iterator();
				while(it.hasNext()){
					Interest interest = it.next();
					Iterator<Router> rit = this.getNextRouterList().iterator();
					while(rit.hasNext()){
						this.forward(rit.next(), interest);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.requestList.isEmpty()){
				flag=0;
			}
		}
	} 

}
