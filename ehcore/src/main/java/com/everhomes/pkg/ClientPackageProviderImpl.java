package com.everhomes.pkg;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@Component
public class ClientPackageProviderImpl implements ClientPackageProvider {
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createPackage(ClientPackage pkg) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
    }

    @Override
    public void updatePackage(ClientPackage pkg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletePackage(ClientPackage pkg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletePackageById(long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<ClientPackageFile> listPackageFiles(long packageId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createPackageFile(ClientPackageFile pkgFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updatePackageFile(ClientPackageFile pkgFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletePackageFile(ClientPackageFile pkgFile) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deletePackageFileById(long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<ClientPackage> listClientPackages(Tuple<String, SortOrder>... orderBy) {
        // TODO Auto-generated method stub
        return null;
    }

}
