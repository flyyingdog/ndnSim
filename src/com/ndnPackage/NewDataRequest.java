package com.ndnPackage;

public class NewDataRequest extends Interest {

	public NewDataRequest() {
		super();
		// TODO Auto-generated constructor stub
		init();
	}
	
	public NewDataRequest(Name name) {
		super(name);
		// TODO Auto-generated constructor stub
		init();
	}

	public NewDataRequest(String str, int srcAddr) {
		super(str, srcAddr);
		// TODO Auto-generated constructor stub
		init();
	}

	public NewDataRequest(String str) {
		super(str);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init(){
		this.setInterestType(NEWDATAREQUEST);
	}

}
