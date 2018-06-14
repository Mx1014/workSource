// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.forum.ForumModuleType;
import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.sensitiveWord.InitSensitiveWordTrieCommand;
import com.everhomes.rest.sensitiveWord.SensitiveWordErrorCodes;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@Component
public class SensitiveWordServiceImpl implements SensitiveWordService, ApplicationListener<ContextRefreshedEvent>{

    private static AhoCorasickDoubleArrayTrie<String> acdat = null;

    private static final String fileName = "C:/Users/Administrator/Desktop/sensitive.txt";
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private SensitiveFilterRecordProvider sensitiveFilterRecordProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void initSensitiveWords(InitSensitiveWordTrieCommand cmd) {
        if (cmd.getUrl() != null && !StringUtils.isBlank(cmd.getUrl())) {
            configurationProvider.setValue("sensitiveword.url",cmd.getUrl());
            init(cmd);
        }
    }

    private void init(InitSensitiveWordTrieCommand cmd) {
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
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        acdat.build(map);
    }

    @Override
    public void filterWords(FilterWordsCommand cmd) {
        cmd.setCreatorUid(UserContext.currentUserId());
        cmd.setNamespaceId(Long.valueOf(UserContext.getCurrentNamespaceId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cmd.setPublishTime(sdf.format(new Date()));
        List<String> wordList = new ArrayList<>();
        if (null != acdat && acdat.size() >0) {
            for (String text : cmd.getTextList()) {
                List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(text.toUpperCase());
                for (AhoCorasickDoubleArrayTrie.Hit<String> hit : hits) {
                    if (!wordList.contains(hit.value)) {
                        wordList.add(hit.value);
                    }
                }
            }
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
        File file = new File(fileName);
        try{
            String url = configurationProvider.getValue(0, "sensitiveword.url", "");
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
            in.close();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }

    private void createSentiveRecord(FilterWordsCommand cmd, List<String> wordList) {
        SensitiveFilterRecord sensitiveFilterRecord = new SensitiveFilterRecord();
        sensitiveFilterRecord.setNamespaceId(cmd.getNamespaceId());
        sensitiveFilterRecord.setSensitiveWords(wordList.toString().substring(1,wordList.toString().length() - 1));
        sensitiveFilterRecord.setModuleId(ForumModuleType.fromCode(cmd.getModuleType()).getModuleId());
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

    private void uploadFileToContentServer() {
        String token = WebTokenGenerator.getInstance().toWebToken(User.SYSTEM_USER_LOGIN);
        File file = new File("D:\\dict.txt");
        try {
            InputStream inputStream = new FileInputStream(file);
            UploadCsFileResponse fileResponse = contentServerService.uploadFileToContentServer(inputStream,"dict.txt",token);
            String url = fileResponse.getResponse().getUrl();
            configurationProvider.setValue("sensitiveword.url",url);
            System.out.println(fileResponse.getResponse().getUrl());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            String url = configurationProvider.getValue(0, "sensitiveword.url", "");
            if (url != null && !StringUtils.isBlank(url)) {
                InitSensitiveWordTrieCommand cmd = new InitSensitiveWordTrieCommand();
                cmd.setUrl(url);
                this.init(cmd);
            }
        }
    }
}
