package com.userJoin.model;

public class UserOutModel {

    /**
     * 自然人身份证
     */
    private String userCard;

    /**
     * 自然人姓名
     */
    private String userName;

    /**
     * 自然人手机号
     */
    private String userPhone;

    /**
     * 社会统一信用代码
     */
    private String CompanyId;

    /**
     * 企业名称
     */
    private String CompanyName;

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    @Override
    public String toString() {
        return "UserOutModel{" +
                "userCard='" + userCard + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", CompanyId='" + CompanyId + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                '}';
    }
}
