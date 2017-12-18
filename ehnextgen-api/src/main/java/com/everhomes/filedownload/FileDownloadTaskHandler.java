package com.everhomes.filedownload;


public interface FileDownloadHandler extends JobHandler{

    /**
     * 用于父接口getRate方法
     */
    Integer rate = 0;

    /**
     * 用于父接口commit方法
     */
    String uri = null;


}
