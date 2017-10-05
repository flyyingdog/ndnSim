package com.ndnPackage;

import java.util.Stack;

public class Interest implements Pack{
	public static final int NORMAL = 0;
	public static final int SYNC = 1;
	public static final int NEWDATAREQUEST = 2;
	public static final int newData = 3;
	
	private int type = INTEREST;
	private int size = INTERESTDEFAULTSIZE;
	private int interestType = NORMAL;
	private int srcAddr;
	private Name name;

	public int getInterestType() {
		return interestType;
	}

	public void setInterestType(int interestType) {
		this.interestType = interestType;
	}
	
	public Interest(){

	}
	
	public Interest(Interest interest){
		this.type = interest.getType();
		this.size = interest.getSize();
		this.interestType = interest.getInterestType();
		this.name = interest.getName();
		this.srcAddr = interest.getSrcAddr();
		
	}
	
	public Interest(Name name){
		this.setName(name);
	}
	
	public Interest(String str){
		Name name= new Name(str);
		this.setName(name);
	}
	
	public Interest(String str,int srcAddr){
		this(str);
		this.setSrcAddr(srcAddr);
	}
	public int getSrcAddr() {
		return srcAddr;
	}
	public void setSrcAddr(int srcAddr) {
		this.srcAddr = srcAddr;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public void setName(Name name){
		this.name = name;
	}
	public Name getName(){
		return this.name;
	}
}
