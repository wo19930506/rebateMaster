package com.edm.utils.comparator;

import java.util.Comparator;
import java.util.Hashtable;

@SuppressWarnings("rawtypes")
public class TypeComparator implements Comparator {

	public int compare(Object a, Object b) {
		Hashtable h1 = (Hashtable) a;
		Hashtable h2 = (Hashtable) b;
		if (((Boolean) h1.get("is_dir")) && !((Boolean) h2.get("is_dir"))) {
			return -1;
		} else if (!((Boolean) h1.get("is_dir")) && ((Boolean) h2.get("is_dir"))) {
			return 1;
		} else {
			return ((String) h1.get("filetype")).compareTo((String) h2.get("filetype"));
		}
	}
}
