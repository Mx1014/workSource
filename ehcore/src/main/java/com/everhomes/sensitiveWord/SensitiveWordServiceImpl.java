// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.forum.ForumModuleType;
import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.sensitiveWord.InitSensitiveWordTrieCommand;
import com.everhomes.rest.sensitiveWord.SensitiveWordErrorCodes;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SensitiveWordServiceImpl implements SensitiveWordService, ApplicationListener<ContextRefreshedEvent>{
    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveWordServiceImpl.class);
    private static AhoCorasickDoubleArrayTrie<String> acdat = null;
    private String fileUrl;
    @Autowired
    private SensitiveFilterRecordProvider sensitiveFilterRecordProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void initSensitiveWords(InitSensitiveWordTrieCommand cmd) {
        if (StringUtils.isNotBlank(cmd.getUrl())) {
            configurationProvider.setValue(ConfigConstants.SENSITIVE_URL,cmd.getUrl());
            configurationProvider.setValue(ConfigConstants.SENSITIVE_FILENAME,cmd.getFileName());
            init(cmd);
        }
    }

    private void init(InitSensitiveWordTrieCommand cmd) {
        try {
            TreeMap<String, String> map = new TreeMap<String, String>();

            if (null == acdat) {
                acdat = new AhoCorasickDoubleArrayTrie<String>();
            }

            BufferedReader in = null;
            try{
                URL realUrl = new URL(cmd.getUrl());
                String s = null;

                URLConnection connection = connect(realUrl);
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((s = in.readLine())!=null){//使用readLine方法，一次读一行
                    map.put(s.trim().toUpperCase(), s.trim().toUpperCase());
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (map.size() > 0) {
                acdat.build(map);
            }
            fileUrl = cmd.getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //校验是否需要重新构建敏感词库
    private void checkIsNeededRebuild() {
        String url = configurationProvider.getValue(0, ConfigConstants.SENSITIVE_URL, "");
        if (!StringUtils.isBlank(url) && !url.equals(fileUrl)) {
            InitSensitiveWordTrieCommand cmd = new InitSensitiveWordTrieCommand();
            cmd.setUrl(url);
            init(cmd);
        }
    }

    @Override
    public void filterWords(FilterWordsCommand cmd) {
        String settingFlag = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.SENSITIVE_SETTING, "");
        if ("false".equals(settingFlag)) {
            return;
        }
        checkIsNeededRebuild();
        if (cmd == null || CollectionUtils.isEmpty(cmd.getTextList())) {
            return;
        }
        cmd.setCreatorUid(UserContext.currentUserId());
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cmd.setPublishTime(sdf.format(new Date()));
        List<String> wordList = new ArrayList<>();
        if (null != acdat && acdat.size() >0) {
            Set<String> wordSet = new HashSet<>();
            for (String text : cmd.getTextList()) {
                String[] chineseWords = text.split("[a-zA-Z]+");
                for (String word : chineseWords) {
                    List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(word);
                    for (AhoCorasickDoubleArrayTrie.Hit<String> hit : hits) {
                        wordSet.add(hit.value);
                    }
                }
                String[] englishWords = text.split("[^a-zA-Z]+");
                for (String word : englishWords) {
                    int index = acdat.exactMatchSearch(word.toUpperCase());
                    if (index > 0) {
                        String hit = acdat.get(index);
                        wordSet.add(hit);
                    }
                }
            }
            wordList.addAll(wordSet);
            if (wordList.size() > 0) {
                createSentiveRecord(cmd, wordList);
                String words = wordList.toString();
                throw RuntimeErrorException.errorWith(SensitiveWordErrorCodes.SCOPE, SensitiveWordErrorCodes.DETECTION_OF_SENSITIVE_WORDS,
                        words.substring(1,words.length()-1));
            }
        }
    }

    @Override
    public File downloadSensitiveWords() {
        BufferedReader in = null;
        BufferedWriter out = null;
        String fileName = configurationProvider.getValue(0, ConfigConstants.SENSITIVE_FILENAME, "");
        String filePath = configurationProvider.getValue(0, ConfigConstants.SENSITIVE_FILEPATH, "");
        if (fileName == null || StringUtils.isBlank(fileName)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "file not exists.");
        }
        File file = new File(filePath+fileName+".txt");
        try{
            String url = configurationProvider.getValue(0, ConfigConstants.SENSITIVE_URL, "");
            URL realUrl = new URL(url);
            String s = null;
            if (!file.exists()) {
                file.createNewFile();
            }
            URLConnection connection = connect(realUrl);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            while((s = in.readLine())!=null){//使用readLine方法，一次读一行
                out.write(s,0,s.length());
                out.newLine();
            }
            out.flush();

        }catch(Exception e){
            LOGGER.error("msg",e);
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                LOGGER.error("msg",e);
                e.printStackTrace();
            }
        }
        return file;
    }

    private void createSentiveRecord(FilterWordsCommand cmd, List<String> wordList) {
        SensitiveFilterRecord sensitiveFilterRecord = new SensitiveFilterRecord();
        sensitiveFilterRecord.setNamespaceId(cmd.getNamespaceId().intValue());
        sensitiveFilterRecord.setSensitiveWords(wordList.toString().substring(1,wordList.toString().length() - 1));
        sensitiveFilterRecord.setModuleId(ForumModuleType.fromCode(cmd.getModuleType()).getModuleId());
        if (cmd.getCommunityId() != null) {
            sensitiveFilterRecord.setCommunityId(cmd.getCommunityId());
        }
        UserInfo user = this.userService.getUserInfo(cmd.getCreatorUid());
        if (user != null) {
            sensitiveFilterRecord.setCreatorUid(cmd.getCreatorUid());
            sensitiveFilterRecord.setCreatorName(user.getNickName());
            if (user.getPhones() != null && user.getPhones().size() > 0) {
                String phones = user.getPhones().toString();
                sensitiveFilterRecord.setPhone(phones.substring(1,phones.length()-1));
            }
        }
        sensitiveFilterRecord.setPublishTime(Timestamp.valueOf(cmd.getPublishTime()));
        sensitiveFilterRecord.setText(cmd.getTextList().toString().substring(1,cmd.getTextList().toString().length()-1));
        this.sensitiveFilterRecordProvider.createSensitiveFilterRecord(sensitiveFilterRecord);
    }

    private URLConnection connect(URL url) {
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 建立实际的连接
            connection.connect();
        } catch (IOException e) {
            LOGGER.error("msg",e);
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            fileUrl = "";
            String url = configurationProvider.getValue(0, ConfigConstants.SENSITIVE_URL, "");
            if (url != null && !StringUtils.isBlank(url)) {
                InitSensitiveWordTrieCommand cmd = new InitSensitiveWordTrieCommand();
                cmd.setUrl(url);
                this.init(cmd);
            }
        }
    }
}
