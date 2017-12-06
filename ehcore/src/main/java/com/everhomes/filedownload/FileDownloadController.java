package com.everhomes.filedownload;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.fleamarket.FleaMarketService;
import com.everhomes.forum.Post;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.fleamarket.FleaMarketPostCommand;
import com.everhomes.rest.fleamarket.FleaMarketUpdateCommand;
import com.everhomes.rest.forum.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/fileDownload")
public class FileDownloadController extends ControllerBase {
    

}
