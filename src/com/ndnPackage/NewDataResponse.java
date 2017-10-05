package com.ndnPackage;

public class NewDataResponse extends Data {

	public NewDataResponse() {
		super();
		// TODO Auto-generated constructor stub
		init();
	}

	public NewDataResponse(Name name) {
		super(name);
		init();
		// TODO Auto-generated constructor stub
	}

	public NewDataResponse(String str) {
		super(str);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public void init(){
		this.setDataType(NEWDATARRESPONSE);
	}

}
