package com.edm.utils.consts;


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
    
    
}
