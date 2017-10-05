package com.ndnPackage;

public class SyncInterest extends Interest {

	public SyncInterest(Name name) {
		super(name);
		// TODO Auto-generated constructor stub
		init();
	}

	public SyncInterest(String str, int srcAddr) {
		super(str, srcAddr);
		// TODO Auto-generated constructor stub
		init();
	}

	public SyncInterest(String str) {
		super(str);
		// TODO Auto-generated constructor stub
		init();
	}

	public SyncInterest() {
		super();
		// TODO Auto-generated constructor stub
		this.init();
	}
	
	public void init(){
		this.setInterestType(SYNC);
	}

}
