// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.sensitiveWord.InitSensitiveWordTrieCommand;

import java.io.File;

public interface SensitiveWordService {

    void initSensitiveWords(InitSensitiveWordTrieCommand cmd);

    void filterWords(FilterWordsCommand cmd);

    File downloadSensitiveWords();
}
