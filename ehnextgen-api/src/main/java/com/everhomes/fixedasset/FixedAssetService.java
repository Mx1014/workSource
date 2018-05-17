package com.everhomes.fixedasset;

import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.fixedasset.BatchDeleteFixedAssetCommand;
import com.everhomes.rest.fixedasset.BatchUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.CreateFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.CreateOrUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.DeleteFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.DeleteFixedAssetCommand;
import com.everhomes.rest.fixedasset.FixedAssetCategoryDTO;
import com.everhomes.rest.fixedasset.FixedAssetDTO;
import com.everhomes.rest.fixedasset.FixedAssetStatisticsDTO;
import com.everhomes.rest.fixedasset.GetFixedAssetCommand;
import com.everhomes.rest.fixedasset.GetFixedAssetDictionaryResponse;
import com.everhomes.rest.fixedasset.GetFixedAssetOperationLogsCommand;
import com.everhomes.rest.fixedasset.GetFixedAssetOperationLogsResponse;
import com.everhomes.rest.fixedasset.GetImportFixedAssetResultCommand;
import com.everhomes.rest.fixedasset.ImportFixedAssetsCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetCategoryCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetCommand;
import com.everhomes.rest.fixedasset.ListFixedAssetResponse;
import com.everhomes.rest.fixedasset.UpdateFixedAssetCategoryCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FixedAssetService {

    GetFixedAssetDictionaryResponse listFixedAssetDictionaries();

    Integer createFixedAssetCategory(CreateFixedAssetCategoryCommand cmd);

    Integer updateFixedAssetCategory(UpdateFixedAssetCategoryCommand cmd);

    void deleteFixedAssetCategory(DeleteFixedAssetCategoryCommand cmd);

    List<FixedAssetCategoryDTO> findFixedAssetCategories(ListFixedAssetCategoryCommand cmd);

    Long createOrUpdateFixedAsset(CreateOrUpdateFixedAssetCommand cmd);

    FixedAssetDTO getFixedAssetDetail(GetFixedAssetCommand cmd);

    void deleteFixedAsset(DeleteFixedAssetCommand cmd);

    Integer batchUpdateFixedAsset(BatchUpdateFixedAssetCommand cmd);

    Integer batchDeleteFixedAsset(BatchDeleteFixedAssetCommand cmd);

    ListFixedAssetResponse listFixedAssets(ListFixedAssetCommand cmd);

    FixedAssetStatisticsDTO getFixedAssetsStatistic(ListFixedAssetCommand cmd);

    GetFixedAssetOperationLogsResponse findFixedAssetOperationLogs(GetFixedAssetOperationLogsCommand cmd);

    HttpServletResponse exportFixedAssets(ListFixedAssetCommand cmd, HttpServletResponse response);

    ImportFileTaskDTO importFixedAssets(ImportFixedAssetsCommand cmd, MultipartFile file);

    ImportFileResponse getImportFixedAssetsResult(GetImportFixedAssetResultCommand cmd);

    void exportImportFileFailResults(GetImportFixedAssetResultCommand cmd, HttpServletResponse httpResponse);

}
