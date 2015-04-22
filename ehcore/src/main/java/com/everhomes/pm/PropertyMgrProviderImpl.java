// @formatter:off
package com.everhomes.pm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;

@Component
public class PropertyMgrProviderImpl implements PropertyMgrProvider {

    @Autowired
    private DbProvider dbProvider;
}
