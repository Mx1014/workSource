// @formatter:off
package com.everhomes.poll;

import com.everhomes.util.StringHelper;

public class PollDTO {
    private Long pollId;
    private Long startTime;
    private Long stopTime;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
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
