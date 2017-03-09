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

    /**
     * 可以指定一个quality, 定义图片输出的质量
     * @param uri
     * @param ownerType
     * @param ownerId
     * @param quality   1~100之间的一个数字，值越小，输出的图片体积越小，清晰度更低
     * @return
     */
    String parserUri(String uri, String ownerType, Long ownerId, Integer quality);

    /**
     * 可以指定输出图片的高度与宽度，如果只指定一个尺寸，则按比例缩放
     * 指定高和宽，则先裁剪，再缩放
     * @param uri
     * @param ownerType
     * @param ownerId
     * @param width     输出图片的宽度
     * @param height    输出图片的高度
     * @return
     */
    String parserUri(String uri, String ownerType, Long ownerId, Integer width, Integer height);

    /**
     * 可以指定输出图片的高度与宽度，和输出质量
     *
     * 如果只指定一个尺寸，则按比例缩放
     * 指定高和宽，则先裁剪，再缩放
     * @param uri
     * @param ownerType
     * @param ownerId
     * @param width     输出图片的宽度
     * @param height    输出图片的高度
     * @param quality   1~100之间的一个数字，值越小，输出的图片体积越小，清晰度更低
     * @return
     */
    String parserUri(String uri, String ownerType, Long ownerId, Integer width, Integer height, Integer quality);

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

}
