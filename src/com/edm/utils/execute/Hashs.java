package com.edm.utils.execute;


public class Hashs {

	/*
	 * 常用字符串哈希函数
	 */
	
	public static int BKDR(String str) {
		int seed = 31;
		int hash = 0;
		int i = 0;
		while (i < str.length()) {
			hash = hash * seed + str.charAt(i);
			i++;
		}

		return hash & 0x7FFFFFFF;
	}
	
}
