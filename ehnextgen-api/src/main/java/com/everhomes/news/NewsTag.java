package com.everhomes.news;

import com.everhomes.server.schema.tables.pojos.EhNewsTag;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */
public class NewsTag extends EhNewsTag {
    private static final long serialVersionUID = -217552533278807071L;

    public NewsTag() {
        super();
    }

    private List<NewsTag> newsTags;

    public List<NewsTag> getNewsTags() {
        return newsTags;
    }

    public void setNewsTags(List<NewsTag> newsTags) {
        this.newsTags = newsTags;
    }
}
