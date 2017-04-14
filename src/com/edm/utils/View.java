package com.edm.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.edm.app.auth.Auth;
import com.edm.entity.Menu;
import com.edm.utils.consts.BrowserMap;
import com.edm.utils.consts.LangMap;
import com.edm.utils.consts.OsMap;
import com.edm.utils.consts.WeekMap;

public class View {
    
    /**
     * LICENSE权限.
     */
    public boolean auth(String link) {
        if (!Auth.isAuth())
            return true;
//        String robot = Auth.ROBOT;
//        String code = Auth.MAP.get(UrlMap.CODE.getAction());
//        return Auth.link(link, robot, code);
        return true;
    }
    
    /**
     * 取值.
     */
    public String value(String value, String defaultValue) {
        if (StringUtils.isNotBlank(value))
            return value;
        return defaultValue;
    }
    
    /**
     * 浏览器.
     */
    public String browser(int id) {
        return BrowserMap.getName(id);
    }

    /**
     * 语言.
     */
    public String lang(int id) {
        return LangMap.getName(id);
    }
    
    /**
     * 系统.
     */
    public String os(int id) {
        return OsMap.getName(id);
    }
    

    /**
     * 周期cron.
     */
    public String cron(String cron) {
        String cronStr = null;
        if (StringUtils.isNotBlank(cron)) {
            String[] crons = StringUtils.splitPreserveAllTokens(cron, " ");
            if (Asserts.empty(crons) || crons.length < 5) {
                return null;
            }

            String minute = crons[0];
            String hour = crons[1];
            String day = crons[2];
            String week = crons[3];
            String month = crons[4];

            // month
            if (!day.equals("*")) {
                cronStr = "每月";
                if (day.equals("-1")) cronStr += "最后一天";
                else cronStr += day + "日";
            }
            // week
            if (!week.equals("*")) {
                cronStr = "每周";
                if (week.equals("1,2,3,4,5")) cronStr += "工作日（周一至周五）";
                else if (week.equals("6,7")) cronStr += "休假（周六至周日）";
                else if (StringUtils.splitPreserveAllTokens(week, ",").length == 1)
                    cronStr += "周" + WeekMap.week(week);
            }
            // day
            if (month.equals("*") && week.equals("*") && day.equals("*")) {
                cronStr = "每日";
            }

            if (StringUtils.isNotBlank(cronStr)) {
                if (hour.length() == 1) hour = "0" + hour;
                if (minute.length() == 1) minute = "0" + minute;
                cronStr += hour + ":" + minute;                
            }
        }
        
        return cronStr;
    }

    
    /**
     * 活跃.
     */
    public String active_cnd(String cnd) {
        if (cnd.equals("-1")) {
            return "不活跃";
        } else if (cnd.equals("1")) {
            return "活跃";
        } else if (cnd.equals("1week")) {
            return "一周以前活跃";
        } else if (cnd.equals("1month")) {
            return "一个月以前活跃";
        } else if (cnd.equals("2month")) {
            return "二个月以前活跃";
        } else if (cnd.equals("3month")) {
            return "三个月以前活跃";
        } else {
            return "全部";
        }
    }

    /**
     * 验证各菜单对应的操作功能权限 </br>
     * 菜单功能增删查改权限，0=全部功能，（1,2,3,4,5,6）=查询、增加、修改、删除、监管、导出
     * @param url
     * 			菜单URL
     * @param funcAuthNum
     * 			 菜单功能增删查改权限,查询=1、增加=2、修改=3、删除=4、监管=5、导出＝6、
     * @return
     */
    public boolean funcAuth(String url, Integer funcAuthNum) {
    	if(StringUtils.isBlank(url) || funcAuthNum==null) return false;
    	List<Menu> menuList = UserSession.getUser().getMenus();
    	if(menuList==null || menuList.size()==0) return false;
    	for(Menu m : menuList) {
    		String menuUrl = m.getUrl();
    		if(url.equals(menuUrl)){
    			Integer[] funcAuths = m.getFuncAuths();
    			// 对 0 进行特别验证，如果是 0 即表示所有权限，无论funcAuthNum值为多少都为true
    			if(funcAuths!=null && funcAuths.length==1 && funcAuths[0]==0) {
					return true;
    			}
    			if(Asserts.hasAny(funcAuthNum, funcAuths)){
    				return true;
    			}else{
    				return false;
    			}
    		}
    	}
    	return false;
    }
    
}
