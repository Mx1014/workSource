// @formatter:off
package com.everhomes.rest.poll;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>pollId:投票ID</li>
 *<li>startTime:起始时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>stopTime:结束时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>pollCount:投票数</li>
 *<li>pollVoterStatus:投票状态 ,1 未投票，2 已投票</li>
 *<li>processStatus:处理状态，0 未知,1 未开始，2 进行中，3 已结束,</li>
 *<li>anonymousFlag:匿名标记 0 不匿名,1 匿名</li>
 *<li>multiChoiceFlag:多选标记</li>
 *<li>uuid:投票唯一的标识</li>
 *<li>wechatPoll: 是否支持微信报名，0-不支持，1-支持 参考   参考{@link com.everhomes.rest.poll.WechatPollFlag }</li>
 *<ul>
 */
public class PollDTO {
    private Long pollId;
    private String startTime;
    private String stopTime;
    private Integer pollCount;
    private Integer anonymousFlag;
    private Integer multiChoiceFlag;
    
    private Integer pollVoterStatus;
    private Integer processStatus;
    
    private String uuid;
    private Byte wechatPoll;
    
    
    public PollDTO() {
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getPollCount() {
        return pollCount;
    }

    public void setPollCount(Integer pollCount) {
        this.pollCount = pollCount;
    }

    public Integer getPollVoterStatus() {
        return pollVoterStatus;
    }

    public void setPollVoterStatus(Integer pollVoterStatus) {
        this.pollVoterStatus = pollVoterStatus;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }
    
    
    public Integer getAnonymousFlag() {
        return anonymousFlag;
    }

    public void setAnonymousFlag(Integer anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }

    public Integer getMultiChoiceFlag() {
        return multiChoiceFlag;
    }

    public void setMultiChoiceFlag(Integer multiChoiceFlag) {
        this.multiChoiceFlag = multiChoiceFlag;
    }
    

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Byte getWechatPoll() {
        return wechatPoll;
    }

    public void setWechatPoll(Byte wechatPoll) {
        this.wechatPoll = wechatPoll;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
