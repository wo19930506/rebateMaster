package com.edm.utils.helper;

import java.util.Map;

import com.edm.modules.cache.Ehcache;
import com.edm.modules.utils.Property;
import com.edm.utils.consts.Config;
import com.edm.utils.consts.Fqn;
import com.edm.utils.consts.Queue;
import com.edm.utils.consts.Sync;
import com.edm.utils.except.Errors;
import com.edm.utils.execute.Counter;
import com.google.common.collect.Maps;

public class Concurrencys {

	@SuppressWarnings("unchecked")
	public static Counter getCounter(Ehcache ehcache, boolean concurrency, String userId) {
		synchronized (Sync.CONCURRENCY) {
			Map<String, Counter> map = (Map<String, Counter>) ehcache.get(Fqn.CONCURRENCY, Queue.CONCURRENCY);
			if (map == null) {
				map = Maps.newHashMap();
				ehcache.put(Fqn.CONCURRENCY, Queue.CONCURRENCY, map);
			}

			if (concurrency) {
				int count = Property.getInt(Config.CONCURRENCY_COUNT);
				
				int processing = 0;
				for (String uId : map.keySet()) {
					Counter counter = map.get(uId);
					if (counter != null) {
						if (counter.getCode() == Counter.PROCESSING) {
							processing++;
						}
					}
				}
				
				if ((processing + 1) > count) {
					throw new Errors("并发数不能多于" + count);
				}
			}
			
			Counter counter = map.get(userId);
			
			if (counter != null && counter.getCode() == Counter.PROCESSING) {
				throw new Errors("正在导入/筛选收件人");
			}

			if (counter == null) {
				counter = new Counter();
			}
			counter.setCode(Counter.PROCESSING);
			map.put(userId, counter);
            
			return counter;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Counter getCounter(Ehcache ehcache, String userId) {
		Map<String, Counter> map = (Map<String, Counter>) ehcache.get(Fqn.CONCURRENCY, Queue.CONCURRENCY);
		if (map == null) {
			return null;
		}
		
		Counter counter = map.get(userId);
		return counter;
	}
	
	@SuppressWarnings("unchecked")
	public static void clearCounter(Ehcache ehcache, boolean except, String userId) {
		Map<String, Counter> map = null;
		synchronized (Sync.CONCURRENCY) {
			map = (Map<String, Counter>) ehcache.get(Fqn.CONCURRENCY, Queue.CONCURRENCY);
			if (map == null) {
				map = Maps.newHashMap();
				ehcache.put(Fqn.CONCURRENCY, Queue.CONCURRENCY, map);
			}
			
			Counter counter = map.get(userId);
            if (counter != null) {
                if (except) {
                    map.remove(userId);
                } else {
                    if (counter.getCode() == Counter.COMPLETED) {
                        map.remove(userId);
                    }
                }
            }
		}
	}
}
