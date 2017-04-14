package com.edm.entity;

/**
 * 操作系统.
 * 
 * @author xiaobo
 */
public class Os {

    private Integer corpId;
    private Integer taskId;
    private Integer templateId;
    private Integer os;
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

    public Integer getOs() {
        return os;
    }
    
    public void setOs(Integer os) {
        this.os = os;
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
