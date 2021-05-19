package com.userJoin.dto;

public class GetUserInfodto {

    /**
     * 目标实现
     */
    private String target;

    /**
     * 授权token
     */
    private String accessToken;

    /**
     * 身份识别码类型 NA 自然人 CP 法人
     */
    private String subjectType;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
}
