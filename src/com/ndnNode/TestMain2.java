package com.ndnNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.ndnPackage.Data;
import com.ndnPackage.Interest;
import com.ndnPackage.Name;

import zkSync.SyncConsumer;

public class TestMain2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<Interest> requestList = new HashSet<Interest>();
		HashSet<Data> responseList = new HashSet<Data>();

	
		SyncConsumer c1 = new SyncConsumer(1,requestList,"/c1");
//		c1.setTempData(new Data("tempData"));
		Router r1 = new Router(2);
		SyncConsumer p1 = new SyncConsumer(3, requestList,"/p1");
		HashMap<Integer,Router> allRouter = new HashMap<Integer,Router>();
		allRouter.put(c1.getAddress(), c1);
		allRouter.put(p1.getAddress(), p1);
		allRouter.put(r1.getAddress(), r1);
		Router.setAllRouter(allRouter);
		Set<String> allRouterName = new HashSet<String>();
		allRouterName.add("/c1");
		SyncConsumer.allRouterName=allRouterName;
		
		System.out.println(Router.getAllRouter().get(p1.getAddress()));
		System.out.println(Router.getAllRouter().get(c1.getAddress()));
		String prefix1 = "/group1/test1/0";
		Name name1 = new Name(prefix1);
		c1.getCs().add(new Data(name1));
		c1.digestLog.add(prefix1);
		c1.currentDigest = c1.digestLog.length(prefix1).toString();
		p1.getCs().add(new Data(name1));
		p1.digestLog.add(prefix1);
		p1.currentDigest = p1.digestLog.length(prefix1).toString();
		c1.add(r1, new Name("/leader"), 1000);
		c1.add(r1, new Name("/ZKsync"), 1000);
		c1.add(r1, 1000);
		r1.add(p1, new Name("/ZKsync"),1000);
		r1.add(p1, new Name("/leader"),1000);
		r1.add(c1, new Name("/Zksync"), 1000);
		r1.add(c1, new Name(c1.getUniqName()),1000);
		p1.add(r1, new Name("/ZKsync"),1000);
		p1.add(r1, new Name(c1.getUniqName()),1000);
		p1.add(c1, 1000);
		p1.setsConsumerType(SyncConsumer.LEADER);
		Thread thread1 = new Thread(c1);
		Thread thread2 = new Thread(p1);
		thread1.start();
		//thread2.start();
	}

}
