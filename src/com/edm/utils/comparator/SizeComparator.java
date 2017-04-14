package com.edm.utils.comparator;

import java.util.Comparator;
import java.util.Hashtable;

@SuppressWarnings("rawtypes")
public class SizeComparator implements Comparator {

	public int compare(Object a, Object b) {
		Hashtable h1 = (Hashtable) a;
		Hashtable h2 = (Hashtable) b;
		if (((Boolean) h1.get("is_dir")) && !((Boolean) h2.get("is_dir"))) {
			return -1;
		} else if (!((Boolean) h1.get("is_dir")) && ((Boolean) h2.get("is_dir"))) {
			return 1;
		} else {
			if (((Long) h1.get("filesize")) > ((Long) h2.get("filesize"))) {
				return 1;
			} else if (((Long) h1.get("filesize")) < ((Long) h2.get("filesize"))) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
