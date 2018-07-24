// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.sensitiveWord.InitSensitiveWordTrieCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensitive")
public class SensitiveWordController extends ControllerBase {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    /**
     *<b>URL: /sensitive/initSensitiveWords</b>
     *<p>初始化敏感词字典</p>
     */
    @RequestMapping("initSensitiveWords")
    @RestReturn(String.class)
    public RestResponse initSensitiveWords(InitSensitiveWordTrieCommand cmd) {
        this.sensitiveWordService.initSensitiveWords(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /sensitive/downloadSensitiveWords</b>
     * <p>下载敏感词库txt文本</p>
     */
    @RequestMapping("downloadSensitiveWords")
    @RestReturn(String.class)
    public RestResponse downloadSensitiveWords() {
        this.sensitiveWordService.downloadSensitiveWords();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
