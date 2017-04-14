package com.edm.utils.consts;


/**
 * 附加权限.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum Permissions {

    BSN(4, "ROLE_BSN"), // 运营专员             :Business
    RDR(6, "ROLE_RDR"); // 企业只读人员   :Reader
    
    private final Integer id;
    private final String name;

    private Permissions(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static final Integer getId(String permissionName) {
        Integer permissionId = null;
        for (Permissions permission : Permissions.values()) {
            if (permission.getName().equals(permissionName)) {
                permissionId = permission.getId();
                break;
            }
        }
        return permissionId;
    }

    public static final String getName(Integer permissionId) {
        String name = "";
        for (Permissions permission : Permissions.values()) {
            if (permission.getId().equals(permissionId)) {
                name = permission.name();
                break;
            }
        }
        return name;
    }
}
