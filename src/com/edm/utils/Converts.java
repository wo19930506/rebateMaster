package com.edm.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.edm.modules.orm.MapBean;
import com.edm.utils.web.R;
import com.edm.utils.web.Validator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Converts {

	public static List<Integer> toIntegers(String str) {
		if (StringUtils.isNotBlank(str)) {
			List<Integer> list = Lists.newArrayList();
			for (String value : StringUtils.split(str, ",")) {
				value = (String) Validator.validate(StringUtils.trim(value), R.INTEGER);
				if (StringUtils.isNotBlank(value)) {
					list.add(Integer.valueOf(value));
				}
			}
			return list;
		}
		return null;
	}
	
	public static List<Integer> toIntegers(MapBean map) {
		if (!Asserts.empty(map)) {
			List<Integer> list = Lists.newArrayList();
			for (Entry<String, Object> entry : map.entrySet()) {
				String value = (String) entry.getValue();
				value = (String) Validator.validate(StringUtils.trim(value), R.INTEGER);
				if (StringUtils.isNotBlank(value)) {
					list.add(Integer.valueOf(value));
				}
			}
			return list;
		}
		return null;
	}

	public static Integer[] _toIntegers(Collection<Integer> coll) {
		if (!Asserts.empty(coll)) {
			Integer[] list = new Integer[coll.size()];
			int count = 0;
			for (Integer value : coll) {
				list[count] = Integer.valueOf(value);
				count++;
			}
			return list;
		}
		return null;
	}
	
	public static Integer[] _toIntegers(String str) {
		if (StringUtils.isNotBlank(str)) {
			StringBuffer sbff = new StringBuffer();
			for (String value : StringUtils.split(str, ",")) {
				value = (String) Validator.validate(StringUtils.trim(value), R.INTEGER);
				if (StringUtils.isNotBlank(value)) {
					sbff.append(value).append(",");
				}
			}
			Integer[] list = new Integer[StringUtils.split(sbff.toString(), ",").length];
			int count = 0;
			for (String value : StringUtils.split(sbff.toString(), ",")) {
				list[count] = Integer.valueOf(value);
				count++;
			}
			return list;
		}
		return null;
	}
	
	public static int[] _toInts(String str) {
		if (StringUtils.isNotBlank(str)) {
			StringBuffer sbff = new StringBuffer();
			for (String value : StringUtils.split(str, ",")) {
				value = (String) Validator.validate(StringUtils.trim(value), R.INTEGER);
				if (StringUtils.isNotBlank(value)) {
					sbff.append(value).append(",");
				}
			}
			int[] list = new int[StringUtils.split(sbff.toString(), ",").length];
			int count = 0;
			for (String value : StringUtils.split(sbff.toString(), ",")) {
				list[count] = Integer.valueOf(value);
				count++;
			}
			return list;
		}
		return null;
	}
	
	public static List<String> toStrings(String str) {
		if (StringUtils.isNotBlank(str)) {
			List<String> list = Lists.newArrayList();
			for (String value : StringUtils.split(str, ",")) {
				list.add(StringUtils.trim(value));
			}
			return list;
		}
		return null;
	}
	
	public static List<String> toStrings(MapBean map) {
		if (!Asserts.empty(map)) {
			List<String> list = Lists.newArrayList();
			for (Entry<String, Object> entry : map.entrySet()) {
				String value = (String) entry.getValue();
				if (StringUtils.isNotBlank(value)) {
					list.add(StringUtils.trim(value));
				}
			}
			return list;
		}
		return null;
	}
	
	public static String[] _toStrings(Collection<String> coll) {
		if (!Asserts.empty(coll)) {
			String[] list = new String[coll.size()];
			int count = 0;
			for (String value : coll) {
				list[count] = value;
				count++;
			}
			return list;
		}
		return null;
	}
	
	public static String[] _toStrings(String str) {
		if (StringUtils.isNotBlank(str)) {
			String[] list = new String[StringUtils.split(str, ",").length];
			int count = 0;
			for (String value : StringUtils.split(str, ",")) {
				list[count] = StringUtils.trim(value);
				count++;
			}
			return list;
		}
		return null;
	}

	public static String toString(List<?> list) {
		if (!Asserts.empty(list)) {
			StringBuffer sbff = new StringBuffer();
			for (Object i : list) {
				sbff.append(i).append(",");
			}
			return StringUtils.removeEnd(sbff.toString(), ",");
		}
		return null;
	}
	
	public static String toString(Object[] objects) {
		if (!Asserts.empty(objects)) {
			StringBuffer sbff = new StringBuffer();
			for (Object i : objects) {
				sbff.append(i).append(",");
			}
			return StringUtils.removeEnd(sbff.toString(), ",");
		}
		return null;
	}
	
	public static String toString(MapBean map) {
		if (!Asserts.empty(map)) {
			StringBuilder sbff = new StringBuilder();
			for (Entry<String, Object> entry : map.entrySet()) {
				String value = (String) entry.getValue();
				if (StringUtils.isNotBlank(value)) {
					sbff.append(StringUtils.trim(value)).append(",");
				}
			}
			return StringUtils.removeEnd(sbff.toString(), ",");
		}
		return null;
	}

	public static String toString(String[] strings) {
		if (strings != null && strings.length > 0) {
			StringBuffer sbff = new StringBuffer();
			for (String value : strings) {
				sbff.append(StringUtils.trim(value)).append(",");
			}
			return StringUtils.removeEnd(sbff.toString(), ",");
		}
		return null;
	}

	public static MapBean toMap(String str) {
		if (StringUtils.isNotBlank(str)) {
			MapBean mb = new MapBean();
			for (String p : StringUtils.split(str, ",")) {
				String key = StringUtils.substringBefore(p, "=");
				String value = StringUtils.substringAfter(p, "=");
				if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
					mb.put(StringUtils.trim(key), StringUtils.trim(value));
				}
			}
			return mb;
		}
		return null;
	}
	
	public static String repeat(String str) {
		if (StringUtils.isNotBlank(str)) {
			MapBean map = new MapBean();
			StringBuilder sb = new StringBuilder();
			for (String value : StringUtils.split(str, ",")) {
				value = StringUtils.trim(value);
				if (!map.containsValue(value)) {
					map.put(value, value);
					sb.append(value).append(",");
				}
			}
			return StringUtils.removeEnd(sb.toString(), ",");
		}
		return null;
	}

	public static Integer[] unique(Integer[] in, Integer[] ex) {
		if (!Asserts.empty(in)) {
			Map<Integer, Integer> exMap = Maps.newHashMap();
			if (!Asserts.empty(ex)) {
				for (Integer value : ex) {
					exMap.put(value, value);
				}
			}
			StringBuilder sb = new StringBuilder();
			for (Integer value : in) {
				if (exMap.containsKey(value)) {
					continue;
				}
				sb.append(value).append(",");
			}
			return _toIntegers(StringUtils.removeEnd(sb.toString(), ","));
		}
		return null;
	}
	
	public static String sort(String str) {
		if (StringUtils.isNotBlank(str)) {
			int len = StringUtils.split(str, ",").length;
			int[] is = new int[len];
			int i = 0;
			for (String value : StringUtils.split(str, ",")) {
				is[i] = Integer.valueOf(StringUtils.trim(value));
				i++;
			}
			Arrays.sort(is);
			StringBuilder sb = new StringBuilder();
			sb.append(",");
			for (int id : is) {
				sb.append(id).append(",");
			}
			return sb.toString();
		}
		return null;
	}

}
