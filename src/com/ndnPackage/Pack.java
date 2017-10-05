package com.ndnPackage;

public interface Pack {
	public final static int INTEREST = 0;
	public final static int DATA = 1;
	public final static int INTERESTDEFAULTSIZE = 10;
	public final static int DATADEFAULTSIZE = 500;
	public abstract int getSrcAddr();
	public abstract int getType();
	public abstract Name getName();
}
