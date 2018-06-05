// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.sensitiveWord.InitSensitiveWordTrieCommand;

public interface SensitiveWordService {

    void initSensitiveWords(InitSensitiveWordTrieCommand cmd);

    void filterWords(FilterWordsCommand cmd);
}
