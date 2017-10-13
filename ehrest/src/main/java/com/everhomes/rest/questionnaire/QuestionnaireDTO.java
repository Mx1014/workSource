// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>问卷调查
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属类型，community</li>
 * <li>ownerId: 所属id，communityId</li>
 * <li>questionnaireName: 名称</li>
 * <li>description: 描述</li>
 * <li>collectionCount: 收集量</li>
 * <li>status: 状态，1草稿，2已发布</li>
 * <li>collectFlag: 问卷收集状态（收集中，收集完成），参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCollectFlagType}</li>
 * <li>targetUserNum: 目标用户数量</li>
 * <li>percentComplete: 完成收集量的百分比</li>
 * <li>posterUri: 封面图uri地址</li>
 * <li>posterUrl: 封面图url地址</li>
 * <li>targetType: 面向对象，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireTargetType}</li>
 * <li>ranges: 对象范围，参考{@link com.everhomes.rest.questionnaire.QuestionnaireRangeDTO}</li>
 * <li>supportAnonymous: 是否支持匿名，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCommonStatus}</li>
 * <li>supportShare: 是否支持分享，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCommonStatus}</li>
 * <li>shareUrl: 分享的URL地址</li>
 * <li>publishTime: 发布时间</li>
 * <li>cutOffTime: 截止时间</li>
 * <li>answeredFlag: 是否已经回答，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCommonStatus}</li>
 * <li>anonymousFlag: 是否匿名回答，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireCommonStatus}</li>
 * <li>createTime: 创建时间</li>
 * <li>submitTime: 企业问卷提交时间</li>
 * <li>questions: 题目列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireQuestionDTO}</li>
 * </ul>
 */
public class QuestionnaireDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private String questionnaireName;
	private String description;
	private Integer collectionCount;
	private Byte status;
	private Byte collectFlag;
	private Integer targetUserNum;
	private String percentComplete;
	private String posterUri;
	private String posterUrl;
	private String targetType;
	@ItemType(QuestionnaireRangeDTO.class)
	private List<QuestionnaireRangeDTO> ranges;
	private Byte supportAnonymous;
	private Byte supportShare;
	private String shareUrl;
	private Long publishTime;
	private Long cutOffTime;
	private Byte answeredFlag;
	private Byte anonymousFlag;
	private Long createTime;
	private Long submitTime;
	@ItemType(QuestionnaireQuestionDTO.class)
	private List<QuestionnaireQuestionDTO> questions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getQuestionnaireName() {
		return questionnaireName;
	}

	public void setQuestionnaireName(String questionnaireName) {
		this.questionnaireName = questionnaireName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public List<QuestionnaireQuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionnaireQuestionDTO> questions) {
		this.questions = questions;
	}

	public Byte getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(Byte collectFlag) {
		this.collectFlag = collectFlag;
	}

	public Integer getTargetUserNum() {
		return targetUserNum;
	}

	public void setTargetUserNum(Integer targetUserNum) {
		this.targetUserNum = targetUserNum;
	}

	public String getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(String percentComplete) {
		this.percentComplete = percentComplete;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public List<QuestionnaireRangeDTO> getRanges() {
		return ranges;
	}

	public void setRanges(List<QuestionnaireRangeDTO> ranges) {
		this.ranges = ranges;
	}

	public Byte getSupportAnonymous() {
		return supportAnonymous;
	}

	public void setSupportAnonymous(Byte supportAnonymous) {
		this.supportAnonymous = supportAnonymous;
	}

	public Byte getSupportShare() {
		return supportShare;
	}

	public void setSupportShare(Byte supportShare) {
		this.supportShare = supportShare;
	}

	public Long getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(Long cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}

	public Byte getAnsweredFlag() {
		return answeredFlag;
	}

	public void setAnsweredFlag(Byte answeredFlag) {
		this.answeredFlag = answeredFlag;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Byte getAnonymousFlag() {
		return anonymousFlag;
	}

	public void setAnonymousFlag(Byte anonymousFlag) {
		this.anonymousFlag = anonymousFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
