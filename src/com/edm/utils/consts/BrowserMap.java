package com.edm.utils.consts;

import java.util.List;
import java.util.Map;

import com.edm.entity.Browser;
import com.google.common.collect.Maps;

/**
 * 浏览器.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum BrowserMap {

    Opera       (1, "Opera"),
    QQ          (2, "QQ"),
    Chrome      (3, "Chrome"),
    UC          (4, "UC"),
    Safari      (5, "Safari"),
    Firefox     (6, "Firefox"),
    _360        (7, "360"),
    IE          (8, "IE"),
    Other       (9, "其他");

    private final Integer id;
    private final String name;

    private BrowserMap(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static final String getName(Integer id) {
        for (BrowserMap e : BrowserMap.values()) {
            if (e.getId().equals(id)) {
                return e.getName();
            }
        }
        return null;
    }
    
    public static final Map<Integer, String> none(List<Browser> browserList) {
        Map<Integer, String> map = Maps.newHashMap();
        for (BrowserMap e : BrowserMap.values()) {
            map.put(e.getId(), e.getName());
        }
        for (Browser b : browserList) {
            if (map.containsKey(b.getBrowser())) {
                map.remove(b.getBrowser());
            }
        }
        return map;
    }
}
