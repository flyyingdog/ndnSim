package com.utils;

public class WRLockImp implements WRLock{
	private int read=0;
	private int write=0;
	@Override
	public void lockR() {
		// TODO Auto-generated method stub
		read++;
	}

	@Override
	public synchronized void lockW() {
		// TODO Auto-generated method stub
		write++;
	}

	@Override
	public synchronized void rlsR() {
		// TODO Auto-generated method stub
		read--;
	}

	@Override
	public synchronized void rlsW() {
		// TODO Auto-generated method stub
		write--;
	}

	@Override
	public synchronized int status() {
		// TODO Auto-generated method stub
		if(write>0) return 2;
		if(read>0) return 1;
		else return 0;
	}

}
