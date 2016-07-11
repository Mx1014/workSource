// @formatter:off
package com.everhomes.pkg;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.pkg.AddClientPackageCommand;
import com.everhomes.rest.pkg.ClientPackageFileDTO;
import com.everhomes.rest.pkg.GetUpgradeFileInfoCommand;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

public interface ClientPackageService {
	/**
	 * List all the package infos in the specific order
	 * @param orderBy order condition
	 * @return package list
	 */
	List<ClientPackageFileDTO> listClientPackages(Tuple<String, SortOrder>... orderBy);
	
	/**
	 * According to the conditions from client, determine a package for it.
	 * @param cmd the conditions to determine a package
	 * @return the package info
	 */
	ClientPackageFileDTO getUpgradeFileInfo(GetUpgradeFileInfoCommand cmd);
	
	/**
	 * Add the new package info to db and save the package files to disk.
	 * @param cmd the package info from client
	 * @param files the file uploaded from client
	 * @return the package info
	 */
	ClientPackageFileDTO addPackage(AddClientPackageCommand cmd, MultipartFile[] files);
	
	/**
	 * Prepare the zip file of package files for downloading
	 * @param pkgId the specific package id
	 * @return the zip file
	 */
	public File preparePackageFile(long pkgId);
}
