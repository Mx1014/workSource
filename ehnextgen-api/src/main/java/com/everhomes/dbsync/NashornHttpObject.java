package com.everhomes.dbsync;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.util.StringHelper;

public class NashornHttpObject implements NashornObject {
    private long id;
    private long createTime;
    private String url;
    private String respStr;
    DeferredResult<String> result;
    private AtomicBoolean finished;

    public NashornHttpObject(DeferredResult<String> result) {
        this.result = result;
        this.createTime = System.currentTimeMillis();
        finished = new AtomicBoolean(false);
    }
    
    public DeferredResult<String> getResult() {
        return result;
    }
    
    public void resp(String rlt) {
        this.respStr = rlt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public void error(Object err) {
        this.result.setErrorResult(err);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getTimeout() {
        return (10*1000l); //TODO hard code here
    }

    @Override
    public void onError(Exception ex) {
        if (finished.getAndSet(true)) {
            return;
        }
        
        this.result.setErrorResult(ex);
    }

    @Override
    public void onComplete() {
      if (finished.getAndSet(true)) {
            return;
        }
        
        if(this.respStr == null) {
            this.result.setErrorResult("response not found");
        } else {
            this.result.setResult(this.respStr);    
        }
    }

    @Override
    public String getJSFunc() {
        return "httpProcess";
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
