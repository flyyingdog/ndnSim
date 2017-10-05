package com.ndnNode;

import com.ndnPackage.Pack;

public class Forward implements Runnable {
	private Pack pack;
	private Router srcRouter;
	private Router toRouter;
	
	public Forward(Pack pack, Router srcRouter, Router toRouter) {
		super();
		this.pack = pack;
		this.srcRouter = srcRouter;
		this.toRouter = toRouter;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
//			System.out.println("router "+srcRouter.getAddress()+" forward pack "+pack.getName().getPrefix() +" to " + toRouter.getAddress());
			this.toRouter.receive(pack);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
