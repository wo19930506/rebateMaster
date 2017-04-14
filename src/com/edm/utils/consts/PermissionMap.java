package com.edm.utils.consts;


/**
 * 权限操作.
 * 
 * @author xiaobo
 */
public enum PermissionMap {

    A (0, "all"),       // 所有
    C (2, "create"),    // 增
    R (1, "read"),      // 查
    U (3, "update"),    // 改
    D (4, "delete"),    // 删
    E (6, "export"),    // 导出
    O (5, "other");     // 其他
    
    private final Integer id;
    private final String name;

    private PermissionMap(Integer id, String name) {
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
        for (PermissionMap e : PermissionMap.values()) {
            if (e.getId().equals(id)) {
                return e.getName();
            }
        }
        return null;
    }
}
