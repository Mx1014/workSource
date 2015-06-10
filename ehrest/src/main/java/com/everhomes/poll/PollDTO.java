// @formatter:off
package com.everhomes.poll;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>pollId:投票ID</li>
 *<li>startTime:起始时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>stopTime:结束时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>pollCount:投票数</li>
 *<li>pollVoterStatus:投票状态</li>
 *<li>processStatus:处状态</li>
 *<ul>
 */
public class PollDTO {
    private Long pollId;
    private String startTime;
    private String stopTime;
    private Integer pollCount;
    
    private Integer pollVoterStatus;
    private Integer processStatus;
    
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
