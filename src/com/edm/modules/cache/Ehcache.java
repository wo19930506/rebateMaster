package com.edm.modules.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class Ehcache {

	public static final String DUMMY_FQN = "";
	public static final String NOTIFICATION_FQN = "notification";

	@Autowired
	private CacheManager manager;

	public void stop() {
		manager.shutdown();
	}

	public void put(String key, Object value) {
		put(DUMMY_FQN, key, value);
	}

	public void put(String fqn, String key, Object value) {
		try {
			if (!manager.cacheExists(fqn)) {
				manager.addCache(fqn);
			}

			Cache cache = manager.getCache(fqn);
			Element element = new Element(key, value);
			remove(fqn, key);
			cache.put(element);
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

	public Object get(String fqn) {
		try {
			if (!manager.cacheExists(fqn)) {
				manager.addCache(fqn);
			}

			Cache cache = manager.getCache(fqn);
			return cache;
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

	public Object get(String fqn, String key) {
		try {
			if (!manager.cacheExists(fqn)) {
				manager.addCache(fqn);
				return null;
			}

			Cache cache = manager.getCache(fqn);
			Element element = cache.get(key);

			if (element != null) {
				return element.getValue();
			}

			return null;
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getValues(String fqn) {
		try {
			if (!manager.cacheExists(fqn)) {
				manager.addCache(fqn);
				return Lists.newArrayList();
			}

			Cache cache = manager.getCache(fqn);
			List values = Lists.newArrayList();
			List keys = cache.getKeys();

            for (Iterator iter = keys.iterator(); iter.hasNext();) {
                Element element = cache.get(iter.next());
                if (element != null) {
                    values.add(element.getValue());
                }
            }

			return values;
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

	public void removeAll(String fqn) {
		Cache cache = manager.getCache(fqn);
		if (cache != null) {
			cache.removeAll();
		}
	}

	public void remove(String fqn, String key) {
		Cache cache = manager.getCache(fqn);
		if (cache != null) {
			cache.remove(key);
		}
	}

	public int getSeconds(String fqn, String key) {
		try {
			if (!manager.cacheExists(fqn)) {
				manager.addCache(fqn);
				return 0;
			}

			Cache cache = manager.getCache(fqn);
			Element element = cache.get(key);

			if (element != null) {
				DateTime start = new DateTime(element.getLastAccessTime());
				DateTime end = new DateTime(element.getExpirationTime());
				return Seconds.secondsBetween(start, end).getSeconds();
			}

			return 0;
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}
}
