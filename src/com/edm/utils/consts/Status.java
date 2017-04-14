package com.edm.utils.consts;

import org.apache.commons.lang.StringUtils;

public class Status {

	public static final int DISABLED = 0;		// 无效/禁用
	public static final int ENABLED = 1;		// 有效/启用
	public static final int FREEZE = 2;			// 冻结
	public static final int DELETED = 3; 		// 删除
	
	public static final int TESTED = 0;         //试用账号
	public static final int NORMAL = 1;         //正常账号
	public static final String REMIND = "1";         //测试账号到期提醒
	// ********** task status **********
	public static final int DRAFTED = 0; 		// 草稿
//	public static final int PLAN = 4; 			// 计划
	
	public static final int PROOFING = 11; 		// 待批示
	public static final int UNAPPROVED = 12; 	// 批示不通过
	public static final int REVIEWING = 13; 	// 待审核
	public static final int RETURN = 14; 		// 审核不通过

	public static final int TEST = 21; 			// 测试发送
	public static final int WAITED = 22; 		// 待发送
	public static final int QUEUEING = 23; 		// 队列中
	public static final int PROCESSING = 24; 	// 发送中
	public static final int PAUSED = 25; 		// 已暂停
	public static final int CANCELLED = 26; 	// 已取消
	public static final int COMPLETED = 27; 	// 已完成
	public static final int NOTFIND = 28;       // 任务文件不存在
	
	/**
	 * 获取审核状态集.
	 * m:waited|completed|none
	 */
    public static Integer[] audit(String m) {
        if (StringUtils.equals(m, "waited")) {
//            if (Securitys.hasAnyRole(new String[] { RoleMap.ADM.getName() })) {
//                Integer[] statuses = { REVIEWING };
//                return statuses;
//            } else {
//                Integer[] statuses = { PROOFING };
//                return statuses;
//            }
            Integer[] statuses = { REVIEWING };
            return statuses;
        } else if (StringUtils.equals(m, "completed")) {
            Integer[] statuses = { UNAPPROVED, RETURN, WAITED, QUEUEING, PROCESSING, 
                    PAUSED, CANCELLED, COMPLETED };
            return statuses;
        } else if (StringUtils.equals(m, "none")) {
            Integer[] statuses = { TEST, QUEUEING, PROCESSING, PAUSED, COMPLETED };
            return statuses;
        } else {
            return null;
        }
    }
    
    /**
     * 获取任务状态集.
     * m:all|proofing|waited|processing|completed
     */
    public static Integer[] task(String m) {
        if (StringUtils.equals(m, "proofing")) {
            Integer[] statuses = { DRAFTED, PROOFING, UNAPPROVED, REVIEWING, RETURN };
            return statuses;
        } else if (StringUtils.equals(m, "waited")) {
            Integer[] statuses = { WAITED };
            return statuses;
        } else if (StringUtils.equals(m, "processing")) {
            Integer[] statuses = { QUEUEING, PROCESSING, PAUSED };
            return statuses;
        } else if (StringUtils.equals(m, "completed")) {
            Integer[] statuses = { CANCELLED, COMPLETED };
            return statuses;
        } else if (StringUtils.equals(m, "processing|completed")) {
            Integer[] statuses = { PROCESSING, PAUSED, CANCELLED, COMPLETED };
            return statuses;
        } else {
            Integer[] statuses = { DRAFTED, PROOFING, UNAPPROVED, REVIEWING, RETURN, WAITED, QUEUEING, PROCESSING, PAUSED, CANCELLED, COMPLETED,NOTFIND};
            return statuses;
        }
    }
    
}
