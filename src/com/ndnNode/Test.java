package com.ndnNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> test = new HashSet<String>();
		System.out.println(test.hashCode());
		test.add("/test1");
		System.out.println(test.hashCode());
		test.add("/test2");
		System.out.println(test.hashCode());
		test.add("/test3");
		System.out.println(test.hashCode());
	}

}
