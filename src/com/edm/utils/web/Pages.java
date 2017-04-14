package com.edm.utils.web;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.edm.modules.orm.MapBean;
import com.edm.modules.orm.Page;
import com.edm.utils.Asserts;
import com.edm.utils.Values;
import com.edm.utils.consts.Const;
import com.edm.utils.except.Errors;

public class Pages {

	public static final String SLIDERS = ",20,40,60,80,100,";
	
	public static void page(HttpServletRequest request, Page<?> page) {
		int pageSize = 0;
		int pageNo = 0;
		
		try {
			Cookie[] cookies = request.getCookies();
			if (!Asserts.empty(cookies)) {
				for (Cookie cookie : cookies) {
					String PSC = Cookies.get(cookie, Cookies.PAGE_SIZE);
					if (StringUtils.isBlank(PSC)) {
						continue;
					}

					PSC = (String) Validator.validate(PSC, R.CLEAN, R.INTEGER);
					PSC = Values.get(PSC, String.valueOf(Const.PAGE_SIZE));
					pageSize = Integer.valueOf(PSC);
				}
			}
			if (!StringUtils.contains(SLIDERS, "," + pageSize + ",")) {
				pageSize = Const.PAGE_SIZE;
			}
		} catch (Errors e) {
			pageSize = Const.PAGE_SIZE;
		}
		
		try {
			pageNo = Values.get(Https.getInt(request, "pageNo", R.CLEAN, R.INTEGER), 1);
		} catch (Errors e) {
			pageNo = 1;
		}
		
		page.setPageSize(pageSize);
		page.setPageNo(pageNo);
	}

	public static void search(MapBean mb, Page<?> page) {
		page.setSearch(!mb.isEmpty());
	}
	
	public static void search(MapBean mb) {
		mb.put("search", !mb.isEmpty());
	}

	public static void order(Page<?> page, String orderBy, String order) {
		if (!page.isOrderBySetted()) {
			page.setOrderBy(orderBy);
			page.setOrder(order);
		}
	}

	public static void order(MapBean mb, String orderBy, String order) {
		mb.put("orderBy", orderBy);
		mb.put("order", order);
	}
	
	public static void put(MapBean mb, String key, String value) {
		if (StringUtils.isNotBlank(value)) {
			mb.put(key, value);
		}
	}

	public static void put(MapBean mb, String key, Integer value) {
		if (value != null) {
			mb.put(key, value);
		}
	}

	public static void put(MapBean mb, String key, Long value) {
		if (value != null) {
			mb.put(key, value);
		}
	}

	public static void put(MapBean mb, String key, Object[] value) {
		if (value != null && value.length > 0) {
			mb.put(key, value);
		}
	}
	
	public static void put(MapBean mb, String key, List<?> value) {
		if (value != null && !value.isEmpty()) {
			mb.put(key, value);
		}
	}
}
