package com.everhomes.test.core.persist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;

@Component
public class DbProviderImpl implements DbProvider {
    @Autowired
    private DSLContext dslContext;
    
    @Autowired
    private DataSource dataSource;

	public DSLContext getDslContext() {
		return this.dslContext;
	}
	
//	@Override
//	public void truncateAllTables() {
//		this.dslContext.meta().getTables().stream().map((r) -> {
//			this.dslContext.truncate(r);
//			return null;
//		});
//	}
	
	@Override
	public void runClassPathSqlFile(String filePath) {
		ClassPathResource resource = new ClassPathResource(filePath);
		
		try {
			runFileSystemSqlFile(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public void runFileSystemSqlDirectory(String dirPath, String suffix) {
		if(dirPath == null) {
			throw new IllegalArgumentException("The directory path may not be null");
		}
		
		List<String> dirPaths = new ArrayList<String>();
		dirPaths.add(dirPath);
		runFileSystemSqlDirectory(dirPaths, suffix);
	}
	
	public void runFileSystemSqlDirectory(List<String> dirPaths, String suffix) {
		if(dirPaths == null) {
			throw new IllegalArgumentException("The directory path list may not be null");
		}
		
		List<String> sqlFileList = new ArrayList<String>();
		for(String dirPath : dirPaths) {
			File dir = new File(dirPath);
			if(!dir.exists()) {
				throw new IllegalArgumentException("The directory not exist, path=" + dir.getAbsolutePath());
			}
			
			if(!dir.isDirectory()) {
				throw new IllegalArgumentException("The path is not a directory, path=" + dir.getAbsolutePath());
			}
			
			searchFileRecursively(sqlFileList, dir, suffix);
		}
		
		runFileSystemSqlFile(sqlFileList);
	}
	
	public void runFileSystemSqlFile(String filePath) {
		List<String> filePaths = new ArrayList<String>();
		filePaths.add(filePath);
		
		runFileSystemSqlFile(filePaths);
	}
    
	public void runFileSystemSqlFile(List<String> filePaths) {
		if(filePaths == null) {
			throw new IllegalArgumentException("The file path list may not be null");
		}
		
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		for (String filePath : filePaths) {
			FileSystemResource resource = new FileSystemResource(filePath);
			populator.addScript(resource);
		}
		
		DatabasePopulatorUtils.execute(populator, this.dataSource);
//		Runtime runtime=Runtime.getRuntime();
//		try{
//			runtime.exec("mysqldump  --no-defaults -uroot -p Pbgpwd#123 ehcore < E:/WorkPlace/Java/J2SE/ehtest-integration/src/test/data/tables/20160522_create_tables.sql");
//		} catch(Exception e) {
//			throw new IllegalStateException(e);
//		}
	}
	
	public void searchFileRecursively(List<String> resultList, File fromFile, String suffix) {
		if(resultList == null) {
			throw new IllegalArgumentException("The result list may not be null");
		}
		
		if(fromFile.isDirectory()) {
			File[] subFiles = fromFile.listFiles();
			for(File subFile : subFiles) {
				searchFileRecursively(resultList, subFile, suffix);
			}
		} else {
			if(suffix == null || suffix.trim().length() == 0 
					|| fromFile.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
				resultList.add(fromFile.getAbsolutePath());
			}
		}
	}
}
