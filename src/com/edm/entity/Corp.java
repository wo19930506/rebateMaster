package com.edm.entity;

import java.util.Date;

/**
 * 企业. auditPath(审核路径. 空值:不需要上级审核, 非空值:需要上级审核. eg. /1/100/需要100和1的企业ID审核)
 * 
 * @author xiaobo
 */
public class Corp {

    private Integer corpId;
    private Integer cosId;
    private String company;
    private String website;
    private String address;
    private String contact;
    private String email;
    private String telephone;
    private String mobile;
    private String fax;
    private String zip;
    private String industry;
    private String emailQuantity;
    private String sendQuantity;
    private String understand;
    private String promise;
    private String agreement;
    private String way;
    private String auditPath;
    private String formalId;
    private String testedId;
    private Integer senderValidate;
    private Integer joinApi;
    private Integer status;
    private String managerId;
    private Date expireTime;
    private Date createTime;
    private Date modifyTime;

    private Integer parentId;

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public Integer getCosId() {
        return cosId;
    }

    public void setCosId(Integer cosId) {
        this.cosId = cosId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getEmailQuantity() {
        return emailQuantity;
    }

    public void setEmailQuantity(String emailQuantity) {
        this.emailQuantity = emailQuantity;
    }

    public String getSendQuantity() {
        return sendQuantity;
    }

    public void setSendQuantity(String sendQuantity) {
        this.sendQuantity = sendQuantity;
    }

    public String getUnderstand() {
        return understand;
    }

    public void setUnderstand(String understand) {
        this.understand = understand;
    }

    public String getPromise() {
        return promise;
    }

    public void setPromise(String promise) {
        this.promise = promise;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getAuditPath() {
        return auditPath;
    }

    public void setAuditPath(String auditPath) {
        this.auditPath = auditPath;
    }

    public String getFormalId() {
        return formalId;
    }

    public void setFormalId(String formalId) {
        this.formalId = formalId;
    }

    public String getTestedId() {
        return testedId;
    }

    public void setTestedId(String testedId) {
        this.testedId = testedId;
    }

    public Integer getSenderValidate() {
        return senderValidate;
    }

    public void setSenderValidate(Integer senderValidate) {
        this.senderValidate = senderValidate;
    }

    public Integer getJoinApi() {
        return joinApi;
    }

    public void setJoinApi(Integer joinApi) {
        this.joinApi = joinApi;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

}
