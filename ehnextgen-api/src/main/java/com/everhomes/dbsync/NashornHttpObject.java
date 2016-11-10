package com.everhomes.dbsync;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.StringHelper;

public class NashornHttpObject implements NashornObject {
    private long id;
    private long createTime;
    private String url;
    private String respStr;
    private String appName;
    private String mapName;
    private String query; //TODO use or not?
    private String body;
    DeferredResult<RestResponse> result;
    private AtomicBoolean finished;

    public NashornHttpObject(DeferredResult<RestResponse> result) {
        this.result = result;
        this.createTime = System.currentTimeMillis();
        finished = new AtomicBoolean(false);
    }
    
    public DeferredResult<RestResponse> getResult() {
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

    public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
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
        
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
        res.setErrorDescription("inner error: " + ex.getMessage());
        this.result.setErrorResult(res);
    }

    @Override
    public void onComplete() {
      if (finished.getAndSet(true)) {
            return;
        }
        
      RestResponse res = new RestResponse();
        if(this.respStr == null) {
        	  res.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
        	  res.setErrorDescription("response not found");
        	  this.result.setErrorResult(res);
        } else {
        	try {
        		Map responseObject = (Map)StringHelper.fromJsonString(this.respStr, Map.class);
        		res.setResponseObject(responseObject);
        		res.setErrorCode(ErrorCodes.SUCCESS);
        		res.setErrorDescription("OK");
        	} catch(Exception ex) {
        		res.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
        		res.setErrorDescription(this.respStr);
        	}
        	
        	this.result.setResult(res);
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
