// @formatter:off
package com.everhomes.configurations;


import com.everhomes.server.schema.tables.pojos.EhConfigurations;
import com.everhomes.util.StringHelper;

public class Configurations extends EhConfigurations {
    
   private static final long serialVersionUID = 4347273082616339932L;
   
   public Configurations(){
	   
   }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
