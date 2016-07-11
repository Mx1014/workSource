package com.everhomes.contentserver;

import com.everhomes.contentserver.MessageHandleRequest;

/**
 * handle static file like image ,audio or video
 * 
 * @author elians
 *
 */
public interface ContentServerMananger {
    /**
     * 
     * @param request
     * @return
     * @throws Exception
     */
    void upload(MessageHandleRequest request) throws Exception;

    /**
     * 
     * @param request
     */
    void delete(MessageHandleRequest request);

    /**
     * 
     * @param request
     * @return
     */
    void auth(MessageHandleRequest request);

}
