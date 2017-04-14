package com.edm.utils.consts;

import java.util.List;
import java.util.Map;

import com.edm.entity.Os;
import com.google.common.collect.Maps;

/**
 * 操作系统.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum OsMap {

    Android         (1, "Android"),
    iPhone          (2, "iPhone"),
    MacOS           (3, "MacOS"),
    Symbian         (4, "Symbian"),
    iPad            (5, "iPad"),
    Windows         (6, "Windows"),
    Linux           (7, "Linux"),
    Windows_Phone   (8, "Windows Phone"),
    Other           (9, "其他");

    private final Integer id;
    private final String name;

    private OsMap(Integer id, String name) {
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
        for (OsMap e : OsMap.values()) {
            if (e.getId().equals(id)) {
                return e.getName();
            }
        }
        return null;
    }
    
    public static final Map<Integer, String> none(List<Os> osList) {
        Map<Integer, String> map = Maps.newHashMap();
        for (OsMap e : OsMap.values()) {
            map.put(e.getId(), e.getName());
        }
        for (Os o : osList) {
            if (map.containsKey(o.getOs())) {
                map.remove(o.getOs());
            }
        }
        return map;
    }
}
