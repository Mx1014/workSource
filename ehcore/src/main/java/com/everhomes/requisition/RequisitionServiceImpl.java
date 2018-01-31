//@formatter:off
package com.everhomes.requisition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Wentian Wang on 2018/1/20.
 */

@Service
public class RequisitionServiceImpl implements RequisitionService {
    @Autowired
    private RequisitionProvider requisitionProvider;
}
