package com.everhomes.pkg;

import java.util.List;

import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

public interface ClientPackageProvider {
	@SuppressWarnings("unchecked")
	List<ClientPackage> listClientPackages(Tuple<String, SortOrder>... orderBy);
	ClientPackage findClientPackageById(long pkgId);
    void createPackage(ClientPackage pkg);
    void updatePackage(ClientPackage pkg);
    void deletePackage(ClientPackage pkg);
    void deletePackageById(long id);

    List<ClientPackageFile> listPackageFiles(long packageId);
    
    void createPackageFile(ClientPackageFile pkgFile);
    void updatePackageFile(ClientPackageFile pkgFile);
    void deletePackageFile(ClientPackageFile pkgFile);
    void deletePackageFileById(long id);
}
