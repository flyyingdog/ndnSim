package com.utils;

public abstract interface WRLock {
	public void lockR();
	public void lockW();
	public void rlsR();
	public void rlsW();
	public int status();
}
