package com.ailin.Pojo;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/****
 * 证照索引表实体</BR>
 * 注：路由标识（route）+证照标识（sourceLicenseId）确定唯一的1条证照记录
 * @author zhoubo
 *
 */
@Table(name = "license_index_reference")
public class LicenseIndexReference implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1819748041396018363L;

//	/**
//     * id 标识
//     */
//    @Id
//    @Column(name = "id")
//	private String id;

    /*****
     * 身份证号码(必填)
     */
    private String cardId;

    /****
     * 数据源编码(必填)    复合主键
     */
    private String route;

    /****
     * 数据源中电子证照标识(必填)		复合主键
     */
    private String certificateIdentifier;

    /****
     * 更新次数
     */
    private Integer count;

    /****
     * 证照编号
     */
    private String certificateNumber;

    /***
     * 证照名称
     */
    private String certificateName;

    /****
     * 数据源证照类型名称
     */
    private String sourceCertificateType;

    /****
     * 数据源证照类型代码
     */
    private String sourceCertificateTypeCode;

    /****
     * 数据源目录id
     */
    private String sourceCatalogId;

    /****
     * 云平台证照类型名称
     */
    private String cloudCertificateType;

    /****
     * 云平台证照类型代码
     */
    private String cloudCertificateTypeCode;
    /****
     * 颁发单位名称
     */
    private String certificateIssuingOrganization;

    /****
     * 颁发时间
     */
    private Date issuedDate;

    /***
     * 有效期开始时间
     */
    private Date effectiveDate;

    /****
     * 有效期截止</BR>
     * 有效期为空时,长期有效
     */
    private Date expiringDate;

    /****
     * 证照状态  详情查看 LicenseState.java 类
     */
    private Integer state;

    /****
     * 证照类别 </BR>
     * 0-批文；1-证照；2-证明；3-证件
     */
    private String category;

    /****
     * 数据源所在区域代码
     */
    private String area;

    /**
     * 数据源证照的更新时间
     */
    private Date sourceUpdateTime;
    
    /**
     * 格式：yyyy-MM-dd HH:mm:ss
     */

    /**
     * 数据源采集推送的证照事件标识（存入证照管理记录）
     */

    /****
     * 创建时间（索引记录创建时间）
     */
    private Date createTime;

    /****
     * 更新时间（路由刷新时间）
     */
    private Date updateTime;

    /****
     * 查询时间
     */
    private Date reqTime;

    /***
     * 上次查询结果(1 成功，0 失败)
     */
    private Integer reqState;

    /****
     * 用户使用频率
     */
    private Integer visitNum;
    

    private String hideFlag;
    
    /**
     * 领取标识 0-否，1-是
     */
    private String collectionFlag;

    /**
     *
     */
    private String fileNumber;

    public LicenseIndexReference(String cardId, String route, String certificateIdentifier, String certificateNumber,
                                 String certificateName, String sourceCertificateType, String sourceCertificateTypeCode,
                                 String sourceCatalogId, Date sourceUpdateTime,
                                 String certificateIssuingOrganization, Integer state,
                                 String category, String area) {
        this.cardId = cardId;
        this.route = route;
        this.certificateIdentifier = certificateIdentifier;
        this.certificateNumber = certificateNumber;
        this.certificateName = certificateName;
        this.sourceUpdateTime = sourceUpdateTime;
        this.sourceCertificateType = sourceCertificateType;
        this.sourceCertificateTypeCode = sourceCertificateTypeCode;
        this.sourceCatalogId = sourceCatalogId;
        this.certificateIssuingOrganization = certificateIssuingOrganization;
        this.state = state;
        this.category = category;
        this.area = area;
    }


    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getCertificateIdentifier() {
        return certificateIdentifier;
    }

    public void setCertificateIdentifier(String certificateIdentifier) {
        this.certificateIdentifier = certificateIdentifier;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getSourceCertificateType() {
        return sourceCertificateType;
    }

    public void setSourceCertificateType(String sourceCertificateType) {
        this.sourceCertificateType = sourceCertificateType;
    }

    public String getSourceCertificateTypeCode() {
        return sourceCertificateTypeCode;
    }

    public void setSourceCertificateTypeCode(String sourceCertificateTypeCode) {
        this.sourceCertificateTypeCode = sourceCertificateTypeCode;
    }

    public String getSourceCatalogId() {
        return sourceCatalogId;
    }

    public void setSourceCatalogId(String sourceCatalogId) {
        this.sourceCatalogId = sourceCatalogId;
    }

    public String getCloudCertificateType() {
        return cloudCertificateType;
    }

    public void setCloudCertificateType(String cloudCertificateType) {
        this.cloudCertificateType = cloudCertificateType;
    }

    public String getCloudCertificateTypeCode() {
        return cloudCertificateTypeCode;
    }

    public void setCloudCertificateTypeCode(String cloudCertificateTypeCode) {
        this.cloudCertificateTypeCode = cloudCertificateTypeCode;
    }

    public String getCertificateIssuingOrganization() {
        return certificateIssuingOrganization;
    }

    public void setCertificateIssuingOrganization(String certificateIssuingOrganization) {
        this.certificateIssuingOrganization = certificateIssuingOrganization;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(Date expiringDate) {
        this.expiringDate = expiringDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getSourceUpdateTime() {
        return sourceUpdateTime;
    }

    public void setSourceUpdateTime(Date sourceUpdateTime) {
        this.sourceUpdateTime = sourceUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public Integer getReqState() {
        return reqState;
    }

    public void setReqState(Integer reqState) {
        this.reqState = reqState;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public String getHideFlag() {
        return hideFlag;
    }

    public void setHideFlag(String hideFlag) {
        this.hideFlag = hideFlag;
    }

    public String getCollectionFlag() {
        return collectionFlag;
    }

    public void setCollectionFlag(String collectionFlag) {
        this.collectionFlag = collectionFlag;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }
}
