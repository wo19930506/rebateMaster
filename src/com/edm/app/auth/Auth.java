package com.edm.app.auth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edm.modules.utils.Property;
import com.edm.modules.utils.encoder.Md5;
import com.edm.utils.consts.Config;
import com.google.common.collect.Maps;

public final class Auth {

	private static final Logger logger = LoggerFactory.getLogger(Auth.class);

	public static String SETUP = "off"; // LICENSE:off, EDM:on
	public static String AUTH = "off"; // LICENSE:on, EDM:off
	
	public static Md5 md5 = new Md5();
	public static String ROBOT = null;
	public static Long SENDS = 0L;
	public static Map<String, String> MAP = Maps.newHashMap();
	
	public static final int SIZE = 1;
//    public static final int LENGTH = 752;
    public static final int LENGTH = 32;
	
	public static void setup() {
		SETUP = "on";
	}
	
	public static boolean isSetup() {
		return SETUP.equals("on");
	}
	
	public static boolean isAuth() {
		return AUTH.equals("on");
	}
	
	public static void robot(String robotPath) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(robotPath), "UTF-8"));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				if (StringUtils.isBlank(line)) {
					continue;
				}
				String key = StringUtils.substringBefore(line, "=");
				String val = StringUtils.substringAfter(line, "=");
				boolean r = StringUtils.equals(md5.encode(StringUtils.upperCase(key)), "cebb21b542877339c40e7e8ecc96796e");
				if (StringUtils.isNotBlank(key) && r) {
					if (StringUtils.isNotBlank(val)) {
						ROBOT = StringUtils.lowerCase(val);
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("(Auth:robot) error: ", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
	public static void load(String licensePath) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(licensePath), "UTF-8"));
			String line = null;
			
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                if (line.length() != LENGTH) {
                    break;
                }
                MAP.put("LICENSE_PASSWD", line);
                break;
            }
            
//			int count = 0;
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				if (count > 1) {
//					MAP.clear();
//					break;
//				}
//
//				if (line.length() != LENGTH) {
//					break;
//				}
//				
//				int start = 32;
//				int end = 40;
//				
//				for (UrlMap mapping : UrlMap.values()) {
//					MAP.put(mapping.getAction(), StringUtils.substring(line, start, end));
//					start = end + 8;
//					end = start + 32;
//				}
//				
//				count++;
//			}
		} catch (Exception e) {
			logger.error("(License:load) error: ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static boolean size() {
		if (MAP.size() != SIZE) {
			return false;
		}

		return true;
	}
	
    public static boolean key(String robot) {
        String uniqueCode = Property.getStr(Config.UNIQUE_CODE);
        logger.error("uniqueCode : "+uniqueCode);
        String licensePasswd = MAP.get("LICENSE_PASSWD");
        logger.error("licensePasswd : "+licensePasswd);
        if (StringUtils.isBlank(uniqueCode)
                || StringUtils.isBlank(robot)
                || StringUtils.isBlank(licensePasswd)) {
            return false;
        }
        logger.error("key : "+md5.encode(md5.encode(robot) + uniqueCode));
        if (!licensePasswd.equals(md5.encode(md5.encode(robot) + uniqueCode))) {
            return false;
        }

        return true;
    }
	
//	public static boolean expire(DateTime now) {
//		String expire = MAP.get(UrlMap.EXPIRE.getAction());
//		if (StringUtils.isBlank(expire)) {
//			return false;
//		}
//
//		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
//		DateTime expireTime = fmt.parseDateTime(MyCode.get(expire));
//
//		if (expireTime.isBefore(now)) {
//			return false;
//		}
//		
//		return true;
//	}
//	
//	public static void sends() {
//		String a = Values.get(validate(MyCode.get(MAP.get(UrlMap.SEND1.getAction()))));
//		String b = Values.get(validate(MyCode.get(MAP.get(UrlMap.SEND2.getAction()))));
//		SENDS = Values.get(Long.valueOf(validate(a + b)));
//	}
//
//	private static String validate(Object target) {
//		return (String) Validator.validate(target, R.CLEAN, R.REQUIRED, R.LONG);
//	}
//
//	public static boolean key(String robot, String code) {
//		if (StringUtils.isBlank(MAP.get(UrlMap.ROBOT.getAction()))) {
//			return false;
//		}
//		if (!MAP.get(UrlMap.ROBOT.getAction()).equals(md5(robot, code, null))) {
//			return false;
//		}
//		
//		return true;
//	}
//
//	public static boolean link(String link, String robot, String code) {
//		String action = UrlMap.getAction(link);
//
//		if (StringUtils.isBlank(action)) {
//			return true;
//		}
//		
//		if (StringUtils.isBlank(MAP.get(action))) {
//			return false;
//		}
//		if (!MAP.get(action).equals(md5(robot, code, action + ":on"))) {
//			return false;
//		}
//		
//		return true;
//	}
//	
//	public static String md5(String robot, String code, String action) {
//		StringBuffer sbff = new StringBuffer();
//		sbff.append("EDM=").append(md5.encode(robot)).append("CODE=").append(md5.encode(code));
//		if (StringUtils.isNotBlank(action))
//			sbff.append("ACTION=").append(md5.encode(action));
//		return md5.encode(sbff.toString());
//	}
	
	public static void main(String[] args) {
        String passwd = md5.encode(md5.encode("78:2b:cb:57:e9:86") + "hello");
        System.out.println(passwd);
        System.out.println(md5.encode("HWADDR"));
        String unicode ="c86b5978605b71d379139d5b2c0645ac";
        String rocot = "B8:AC:6F:7E:B6:71";
        System.out.println(md5.encode(md5.encode(rocot) + unicode));
        
    }
}
