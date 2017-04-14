package com.edm.utils.consts;


/**
 * 语言环境.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum LangMap {
    
    SimpleChina         (1, "简体中文"),
    TraditionalChina    (2, "繁体中文"),
    English             (3, "英语"),
    French              (4, "法语"),
    German              (5, "德语"),
    Japanese            (6, "日语"),
    Korean              (7, "韩语"),
    Spanish             (8, "西班牙语"),
    Swedish             (9, "瑞典语"),
    Other               (10, "其他");

    private final Integer id;
    private final String name;

    private LangMap(Integer id, String name) {
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
        for (LangMap e : LangMap.values()) {
            if (e.getId().equals(id)) {
                return e.getName();
            }
        }
        return null;
    }
    
   
}
