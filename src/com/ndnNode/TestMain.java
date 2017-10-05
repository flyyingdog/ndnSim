package com.ndnNode;

import java.util.HashMap;
import java.util.HashSet;

import com.ndnPackage.Data;
import com.ndnPackage.Interest;
import com.ndnPackage.Name;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<Interest> requestList = new HashSet<Interest>();
		HashSet<Data> responseList = new HashSet<Data>();
		Name test1 = new Name("/test1");
		Name test2 = new Name("/test2");
		Interest n1 = new Interest("/test1");n1.setSize(1);
		Interest n2 = new Interest("/test2");n2.setSize(1);
		Data d1 = new Data("/test1");d1.setSize(10);
		Data d2 = new Data("/test2");d2.setSize(10);
		requestList.add(n1);
//		requestList.add(n2);
		responseList.add(d1);
		responseList.add(d2);
	
		Consumer c1 = new Consumer(1,requestList);
		Router r1 = new Router(2);
		Producer p1 = new Producer(3,responseList);
		HashMap<Integer,Router> allRouter = new HashMap<Integer,Router>();
		allRouter.put(c1.getAddress(), c1);
		allRouter.put(p1.getAddress(), p1);
		allRouter.put(r1.getAddress(), r1);
		Router.setAllRouter(allRouter);
		
		System.out.println(Router.getAllRouter().get(p1.getAddress()));
		System.out.println(Router.getAllRouter().get(c1.getAddress()));
		c1.add(r1, test1, 1000);
		c1.add(r1, test2, 1000);
		c1.add(r1, 1000);
		r1.add(p1, test1,1000);
//		r1.add(p1, test2, 1000);
		p1.add(c1, 1000);
		Thread thread1 = new Thread(c1);
		Thread thread2 = new Thread(p1);
		thread1.start();
		thread2.start();
	}

}
