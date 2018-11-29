package com.everhomes.rest.openapi.zhenzhihui;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

public class TicketCommand {
    @NotNull
    private Long id;
    private Integer userId;
    private String ticket;
    private String content;

    public TicketCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
