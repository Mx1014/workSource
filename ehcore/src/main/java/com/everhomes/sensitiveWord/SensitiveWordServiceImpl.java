// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.sensitiveWord.InitSensitiveWordTrieCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.WebTokenGenerator;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Component
public class SensitiveWordServiceImpl implements SensitiveWordService{

    private static AhoCorasickDoubleArrayTrie<String> acdat;

    private static String url;

    private static final String fileName = "C:/Users/Administrator/Desktop/sensitive.txt";
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private SensitiveFilterRecordProvider sensitiveFilterRecordProvider;
    @Autowired
    private UserService userService;

    @Override
    public void initSensitiveWords(InitSensitiveWordTrieCommand cmd) {
        TreeMap<String, String> map = new TreeMap<String, String>();

        if (null == acdat) {
            acdat = new AhoCorasickDoubleArrayTrie<String>();
        }
        BufferedReader in = null;
        try{
            URL realUrl = new URL(cmd.getUrl());
            url = cmd.getUrl();
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
        List<String> wordList = new ArrayList<>();
        if (null == acdat) {
            acdat = new AhoCorasickDoubleArrayTrie<String>();
        }
        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(cmd.getText().toUpperCase());
        for (AhoCorasickDoubleArrayTrie.Hit<String> hit : hits) {
            wordList.add(hit.value);
        }
        if (wordList.size() > 0) {
            createSentiveRecord(cmd, wordList);
        }
    }

    @Override
    public File downloadSensitiveWords() {
        BufferedReader in = null;
        BufferedWriter out = null;
        File file = new File(fileName);
        try{
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
        sensitiveFilterRecord.setAppId(cmd.getAppId());
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
        sensitiveFilterRecord.setText(cmd.getText());
        this.sensitiveFilterRecordProvider.createSensitiveFilterRecord(sensitiveFilterRecord);
    }

    private void uploadFileToContentServer() {
        String token = WebTokenGenerator.getInstance().toWebToken(User.SYSTEM_USER_LOGIN);
        File file = new File("D:\\dict.txt");
        try {
            InputStream inputStream = new FileInputStream(file);
            UploadCsFileResponse fileResponse = contentServerService.uploadFileToContentServer(inputStream,"dict.txt",token);
            url = fileResponse.getResponse().getUrl();
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

}
