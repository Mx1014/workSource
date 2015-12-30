// @formatter:off
package com.everhomes.pkg;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SortField;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.jooq.JooqHelper;
import com.everhomes.rest.pkg.AddClientPackageCommand;
import com.everhomes.rest.pkg.ClientPackageFileDTO;
import com.everhomes.rest.pkg.GetUpgradeFileInfoCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.FileHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import com.everhomes.util.ZipHelper;

@Component
public class ClientPackageServiceImpl implements ClientPackageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientPackageServiceImpl.class);
    
    @Autowired
    private ClientPackageProvider clientPackageProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    /** The default path where will store all the package files uploaded */
    public final static String PKG_STORE_PATH = "pkg.storepath";
    
    /** The flag indicate whether the package is uploaded in debug mode */
    public final static String PKG_DEBUG_FLAG = "pkg.debug";
    
    /** The zip file flag, it will be used as part of the zip file path */
    public final static String ZIP_FILE_FLAG = "zip";
    
    /** The uncompressed package file flag, it will be used as part of the file path */
    public final static String PKG_FILE_FLAG = "files";
    
    @SuppressWarnings("all") 
    @Override
    public List<ClientPackageFileDTO> listClientPackages(Tuple<String, SortOrder>... orderBy) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	SortField[] orderByFields = JooqHelper.toJooqFields(Tables.EH_CLIENT_PACKAGES, orderBy);
    	
    	List<ClientPackageFileDTO> result;
    	SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CLIENT_PACKAGES);
    	if(orderByFields != null) {
            result = selectStep.orderBy(orderByFields).fetch().map(
                new DefaultRecordMapper(Tables.EH_CLIENT_PACKAGES.recordType(), ClientPackageFileDTO.class)
            );
        } else {
            result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CLIENT_PACKAGES.recordType(), ClientPackageFileDTO.class)
            );
        }
    	
    	return result;
    }
    
    @SuppressWarnings("all")
	@Override
    public ClientPackageFileDTO getUpgradeFileInfo(GetUpgradeFileInfoCommand cmd) {
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        List<ClientPackageFileDTO> result;
        
        SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_CLIENT_PACKAGES);
        Condition condition = null;
        
    	String name = cmd.getName();
        if(name != null && !name.isEmpty()) {
        	condition = Tables.EH_CLIENT_PACKAGES.NAME.eq(name);
        }
        
        long versionCode = cmd.getVersionCode();
        if(versionCode > 0) {
            if(condition != null)
                condition = condition.and(Tables.EH_CLIENT_PACKAGES.VERSION_CODE.eq(versionCode));
            else
                condition = Tables.EH_CLIENT_PACKAGES.VERSION_CODE.eq(versionCode);
        }
        
        byte packageEdition = cmd.getPackageEdition();
        if(packageEdition > 0) {
            if(condition != null)
                condition = condition.and(Tables.EH_CLIENT_PACKAGES.PACKAGE_EDITION.eq(packageEdition));
            else
                condition = Tables.EH_CLIENT_PACKAGES.PACKAGE_EDITION.eq(packageEdition);
        }
        
        byte devicePlatform = cmd.getDevicePlatform();
        if(devicePlatform > 0) {
            if(condition != null)
                condition = condition.and(Tables.EH_CLIENT_PACKAGES.DEVICE_PLATFORM.eq(devicePlatform));
            else
                condition = Tables.EH_CLIENT_PACKAGES.DEVICE_PLATFORM.eq(devicePlatform);
        }
        
        int distributionChannel = cmd.getDistributionChannel();
        if(distributionChannel > 0) {
            if(condition != null)
                condition = condition.and(Tables.EH_CLIENT_PACKAGES.DISTRIBUTION_CHANNEL.eq(distributionChannel));
            else
                condition = Tables.EH_CLIENT_PACKAGES.DISTRIBUTION_CHANNEL.eq(distributionChannel);
        }
        
        String tag = cmd.getTag();
        if(tag != null && !tag.isEmpty()) {
            if(condition != null)
                condition = condition.and(Tables.EH_CLIENT_PACKAGES.TAG.eq(tag));
            else
                condition = Tables.EH_CLIENT_PACKAGES.TAG.eq(tag);
        }
        
        if(condition != null) {
            selectStep.where(condition);
        }
        
        result = selectStep.fetch().map(
                new DefaultRecordMapper(Tables.EH_CLIENT_PACKAGES.recordType(), ClientPackageFileDTO.class)
        );
        
        if(result == null || result.size() == 0) {
        	return null;
        } else {
        	return result.get(0);
        }
    }
    
    @Override
    public ClientPackageFileDTO addPackage(AddClientPackageCommand cmd, MultipartFile[] files) {
    	User creator = UserContext.current().getUser();
        long creatorId = creator.getId();
        
        if(files == null || files.length == 0) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, no file is uploaded");
        }
        
        String storePath = configurationProvider.getValue(PKG_STORE_PATH, "");
        if(storePath == null || storePath.isEmpty()) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid configuration, the store path is not found");
        }
        
        ClientPackage pkg = createNewClientPackage(cmd, creatorId);
        clientPackageProvider.createPackage(pkg);
        
        long pkgId = pkg.getId();
    	File storeBaseDir = new File(storePath, String.valueOf(pkgId));
        File zipFile = savePackageFile(storeBaseDir, files);
        if(zipFile == null) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Failed to save the package file");
        }
        
        // The files in zip file need to be edited by developers when it's in debug mode, 
        // so extract the files after zip file is uploaded
        boolean isDebug = configurationProvider.getBooleanValue(PKG_DEBUG_FLAG, false);
        if(isDebug) {
        	decompressPackageZipFile(zipFile, storeBaseDir);
        }
        
        return ConvertHelper.convert(pkg, ClientPackageFileDTO.class);
    }
    
    @Override
    public File preparePackageFile(long pkgId) {
    	ClientPackage pkg = clientPackageProvider.findClientPackageById(pkgId);
    	if(pkg == null) {
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, the package is not found for id " + pkgId);
    	}
        
        String storePath = configurationProvider.getValue(PKG_STORE_PATH, "");
        if(storePath == null || storePath.isEmpty()) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Invalid configuration, the store path is not found");
        }
    	
    	File storeBaseDir = new File(storePath, String.valueOf(pkgId));
    	
        boolean isDebug = configurationProvider.getBooleanValue(PKG_DEBUG_FLAG, false);
    	File zipFileDir = new File(storeBaseDir, ZIP_FILE_FLAG);
        File zipFile = null;
        
        if(isDebug) {
        	// The package file may be edited by developers when it's in debug mode, 
            // so compress the package file before downloaded.
        	if(zipFileDir.exists()) {
        		File[] childFiles = zipFileDir.listFiles();
        		for(File childFile : childFiles) {
        			FileHelper.deleteFile(childFile);
        		}
        	}
        	zipFile = decompressPackageZipFile(storeBaseDir);
        } else {
        	// When it's not in debug mode, find the first zip file directly
        	if(zipFileDir.exists()) {
        		File[] childFiles = zipFileDir.listFiles();
        		for(File childFile : childFiles) {
        			String fileName = childFile.getName();
        			if(fileName.equalsIgnoreCase(ZIP_FILE_FLAG)) {
        				zipFile = childFile;
        				break;
        			}
        		}
        	}
        }
        
        return zipFile;
    }
    
    /**
     * Save the package file to disk, the path format: <sotrePath>/<pkgId>/<originalPackageName>
     * @param storeBaseDir The base directory of storing package file
     * @param files The package file object
     * @return zip file if saved successfully, null otherwise
     */
    private File savePackageFile(File storeBaseDir, MultipartFile[] files) {
        File zipFileDir = new File(storeBaseDir, ZIP_FILE_FLAG);
        
        String zipFileName = files[0].getOriginalFilename();
        File zipFile = new File(zipFileDir, zipFileName);
        
        try {
        	if(FileHelper.copyFile(files[0].getInputStream(), zipFile)) {
            	return zipFile;
            } 
        } catch(Exception e) {
        	LOGGER.error("Failed to save package file, storeBaseDir=" + storeBaseDir + ", file=" + files[0].getOriginalFilename(), e);
        }
        
        return null;
    }
    
    private void decompressPackageZipFile(File zipFile, File storeBaseDir) {
    	File pkgFileDir = new File(storeBaseDir, PKG_FILE_FLAG);
    	ZipHelper.decompress(zipFile.getAbsolutePath(), pkgFileDir.getAbsolutePath());
    }
    
    private File decompressPackageZipFile(File storeBaseDir) {
    	File pkgFileDir = new File(storeBaseDir, PKG_FILE_FLAG);
    	File zipFile = new File(storeBaseDir, pkgFileDir.getName()+".zip");
    	ZipHelper.compress(zipFile.getAbsolutePath(), pkgFileDir.getAbsolutePath());
    	
    	return zipFile;
    }
    
    private ClientPackage createNewClientPackage(AddClientPackageCommand cmd, long creatorId) {
    	ClientPackage pkg = new ClientPackage();
    	
    	pkg.setName(cmd.getName());
    	pkg.setVersionCode(cmd.getVersionCode());
    	pkg.setPackageEdition(cmd.getPackageEdition());
    	pkg.setDevicePlatform(cmd.getDevicePlatform());
    	pkg.setDistributionChannel(cmd.getDistributionChannel());
    	pkg.setTag(cmd.getTag());
    	pkg.setJsonParams(cmd.getJsonParams());
    	pkg.setStatus(ClientPackageStatus.ACTIVE.getCode());
        pkg.setCreatorUid(creatorId);
        pkg.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        return pkg;
    }

}
