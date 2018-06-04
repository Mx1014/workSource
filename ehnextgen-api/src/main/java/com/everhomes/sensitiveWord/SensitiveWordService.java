// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.rest.sensitiveWord.FilterWordsCommand;

public interface SensitiveWordService {

    void filterWords(FilterWordsCommand cmd);

    void uploadFileToContentServer();
}
