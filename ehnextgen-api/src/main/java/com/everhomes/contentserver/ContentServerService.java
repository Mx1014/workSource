package com.everhomes.contentserver;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.contentserver.AddConfigItemCommand;
import com.everhomes.rest.contentserver.AddContentServerCommand;
import com.everhomes.rest.contentserver.ContentServerDTO;
import com.everhomes.rest.contentserver.UpdateContentServerCommand;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.messaging.ImageBody;

public interface ContentServerService {

    ContentServer addContentServer(AddContentServerCommand cmd);

    void removeContentServer(Long contentServerId);

    void updateContentServer(UpdateContentServerCommand cmd);

    List<ContentServerDTO> listContservers();

    void addConfigItem(AddConfigItemCommand item);
    void removeConfigItem(String itemName);
    ContentServer selectContentServer() throws Exception;

    List<String> parserUri(List<String> uris, String ownerType, Long ownerId);

    String parserUri(String uri, String ownerType, Long ownerId);

    ContentServerResource findResourceByUri(String uri);
    
    ImageBody parserImageBody(String uri, String ownerType, Long ownerId);
    
    String getContentServer();
    
    List<UploadCsFileResponse> uploadFileToContentServer(MultipartFile[] files);
    
    /**
     * 
     * <p>把文件（图片、语音、视频、普通文件）上传到Content Server，并得到结果URI和URL。</p>
     * @param fileStream 文件流
     * @param fileSuffix 文件后缀
     * @param token 用户登录token
     * @return 如果上传成功则返回
     */
    UploadCsFileResponse uploadFileToContentServer(InputStream fileStream, String fileName, String token);

	String parserUri(String uri);

}
