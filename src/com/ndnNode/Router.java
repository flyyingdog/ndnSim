package com.ndnNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.ndnPackage.Data;
import com.ndnPackage.Interest;
import com.ndnPackage.Name;
import com.ndnPackage.Pack;

public class Router implements Runnable {
	public final int DEFAULTDELAYTIME = 100;
	public static Map<Integer,Router> allRouter = new HashMap<Integer,Router>();
	
	private Cs cs;
	private Fib fib;
	protected Pit pit;
	private int address;
	public Map<Router,Integer> delayTimeMap = new HashMap<Router,Integer>();
	public HashSet<Router> nextRouterList;
	
	public HashSet<Router> getNextRouterList() {
		return nextRouterList;
	}

	public void setNextRouterList(HashSet<Router> nextRouterList) {
		this.nextRouterList = nextRouterList;
	}

	public Router(){
		this.address = new Random().nextInt(100);
		this.nextRouterList = new HashSet<Router>();
		this.cs = new Cs();
		this.fib = new Fib();
		this.pit = new Pit();
	}
	
	public Router(int address){
		this();
		this.address = address;
	}
	
	//create topolate
	public void add(Router router,Name name,int delayTime){
		this.fib.add(name, router);
		this.delayTimeMap.put(router, delayTime);
	}
	
	public void add(Router router,int delayTime){
		this.getNextRouterList().add(router);
		this.delayTimeMap.put(router, delayTime);
	}
	
	//根据路由转发interest和data
	public void forward(Router router,Interest interest){
		System.out.println("router " + this.getAddress() + " forward interest "+interest.getName() + " to "+router.getAddress());
		interest.setSrcAddr(this.getAddress());
		Thread thread1 = new Thread(new Forward(interest,this,router));
		thread1.start();
	}
	
	public void forward(Router router,Data data) throws InterruptedException{
		System.out.println("router " + this.getAddress() + " forward data "+data.getName() + " to "+router.getAddress());
		data.setSrcAddr(this.getAddress());
//		router.receive(data,this.address);
		new Thread(new Forward(data,this,router)).start();
	}
	
	public void responseDataByPit(Data data){
		Name name = data.getName();
		HashSet<Router> routerList = this.pit.get(name);
		if(routerList!=null){
			for(Router router:routerList){
				try {
					this.forward(router, data);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//接收到interest后，先判断CS，然后根据fib表转发interest，最后添加pib表
	public void receive(Interest interest) throws InterruptedException{
		System.out.println("router" + this.getAddress() + " receive interest " + interest.getName() + " from "+ interest.getSrcAddr());
		Data data = cs.get(interest);
		int srcAddr = interest.getSrcAddr();
		Router router = allRouter.get(srcAddr);
		Interest forwardInterest = new Interest(interest);
		forwardInterest.setSrcAddr(this.address);
		if(data!=null){
			if(router!=null){
				this.forward(router, data);
			}
		}else{
			if(this.pit.get(interest)==null){
				this.pit.add(interest, router);
				HashSet<Router> routerList = this.getFib().get(interest);
				if (routerList != null){
					Iterator<Router> it = routerList.iterator();
					while(it.hasNext()){
						Router nextRouter = it.next();
						this.forward(nextRouter, forwardInterest);
					}
				}
			}
			this.pit.add(interest, router);
		}
	}
	
	public void receive(Data data,int srcAddr) throws InterruptedException{
		System.out.println("router" + this.getAddress() + " receive Data " + data.getName() + " from "+ srcAddr);

		HashSet<Router> routerList = this.pit.get(data);
		if(routerList != null){
			System.out.println("routerList!=null");
			Iterator<Router> it = routerList.iterator();
			while(it.hasNext()){
				Router router = it.next();
//				System.out.println(router.getAddress());
				this.forward(router, data);
			}
			this.pit.remove(data);
		}
	}
	
	public void receive(Pack pack) throws InterruptedException{
		if(pack.getType()==Pack.INTEREST){
			this.receive((Interest)pack);
		}else{
			this.receive((Data)pack,pack.getSrcAddr());
		}
	}
	
	//根据fib转发interest
	public void sendInterestByFib(Interest interest){
//		String prefix = interest.getName().getPrefix();
		HashSet<Router> routerList = this.getFib().get(interest);
		if (routerList != null){
			Iterator<Router> it = routerList.iterator();
			while(it.hasNext()){
				Router nextRouter = it.next();
				this.forward(nextRouter, interest);
			}
		}
	}
	
	//根据pit转发data
	public void sendDataByPit(Data data) throws InterruptedException{
		HashSet<Router> routerList = this.pit.get(data);
		if(routerList != null){
			System.out.println("routerList!=null");
			Iterator<Router> it = routerList.iterator();
			while(it.hasNext()){
				Router router = it.next();
				System.out.println(router.getAddress());
				this.forward(router, data);
			}
		}
	}
	
	//更新pit表
	public void insertPit(Interest interest,Integer srcAddr){
		Router router = allRouter.get(srcAddr);
		this.pit.add(interest, router);
	}
	
	//去除pit
	public void deletePit(Name name){
		this.pit.remove(name);
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + address;
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
		Router other = (Router) obj;
		if (address != other.address)
			return false;
		return true;
	}

	public Cs getCs() {
		return cs;
	}
	
	public void setCs(Cs cs) {
		this.cs = cs;
	}
	
	public Fib getFib() {
		return fib;
	}
	
	public void setFib(Fib fib) {
		this.fib = fib;
	}
	
	public Pit getPit() {
		return pit;
	}
	
	public void setPit(Pit pit) {
		this.pit = pit;
	}
	
	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public static Map<Integer, Router> getAllRouter() {
		return allRouter;
	}

	public  static void setAllRouter(Map<Integer, Router> allRouter) {
		Router.allRouter = allRouter;
	}

	public Map<Router, Integer> getDelayTimeMap() {
		return delayTimeMap;
	}

	public void setDelayTimeMap(Map<Router, Integer> delayTimeMap) {
		this.delayTimeMap = delayTimeMap;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
