package com.edm.entity;

/**
 * 语言环境.
 * 
 * @author xiaobo
 */
public class Lang {

    private Integer corpId;
    private Integer taskId;
    private Integer templateId;
    private Integer lang;
    private Integer openCount;
    private Integer clickCount;

    public Integer getCorpId() {
        return corpId;
    }
    
    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public Integer getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getLang() {
        return lang;
    }
    
    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public Integer getOpenCount() {
        return openCount;
    }
    
    public void setOpenCount(Integer openCount) {
        this.openCount = openCount;
    }

    public Integer getClickCount() {
        return clickCount;
    }
    
    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

}
