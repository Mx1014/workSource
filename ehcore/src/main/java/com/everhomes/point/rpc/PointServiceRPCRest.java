package com.everhomes.point.rpc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.everhomes.rest.point.GetUserPointCommand;
import com.everhomes.rest.point.PointScoreDTO;

@Service
public class PointServiceRPCRest extends PointServerRPCRestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointServiceRPCRest.class);
	
    @SuppressWarnings("unused")
	public PointScoreDTO getUserPoint(GetUserPointCommand cmd) {  
   	
    	PointScoreResponse resp =
                call(URIConstants.POINTSCORE_GETUSERPOINT_URL, cmd, PointScoreResponse.class);
    	
    	LOGGER.info(resp.toString());
    	if(resp != null)
    	{
    		return resp.getPointScoreDTO() ;
    	}
    	return null ;
    }

    

}
