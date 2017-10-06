package com.everhomes.salary;

import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SalaryEmployeesSearcher;

/**
 * Created by M on 2017/7/26.
 */
public class SalaryEmployeesSearcherImpl extends AbstractElasticSearch implements SalaryEmployeesSearcher {

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void syncUniongroupDetailsIndexs() {

    }

    @Override
    public String getIndexType() {
        return null;
    }
}
