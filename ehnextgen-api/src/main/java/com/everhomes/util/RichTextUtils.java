package com.everhomes.util;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xq.tian on 2018/3/22.
 */
public class RichTextUtils {

    private final static Pattern imageTagPattern = Pattern.compile("<img src=\"(.*?)\".*?/>");

    /**
     * <pre>
     * 将富文本里的图片链接替换成对应的链接
     *
     * {@code
     *
     *      String rt = "<p>测试简介1</p><p><img src=\"https://content-1.zuolin.com/xxx/xxx\"/>";
     *      String processed = RichTextUtils.processImages(rt, csURI -> {
     *          // do some your action
     *          return doSomeYourAction(url);
     *      });
     * }
     * </pre>
     * @param richText  富文本
     * @param func  处理函数
     * @return 处理过后的富文本
     */
    public static String processImages(String richText, Function<String, String> func) {
        return process(imageTagPattern, richText, func);
    }

    public static String process(Pattern p, String richText, Function<String, String> func) {
        if (richText == null || richText.isEmpty()) {
            return richText;
        }

        StringBuilder sb = new StringBuilder(richText.length());
        Matcher m = p.matcher(richText);

        int start = 0;
        int end;
        while (m.find()) {
            end = m.start(1);

            String sub = richText.substring(start, end);
            String url = m.group(1);
            String newUrl = null;
            try {
                newUrl = func.apply(http2cs(url));
            } catch (Exception e) {
                newUrl = url;
            }
            sb.append(sub).append(newUrl);

            start = end + url.length();
        }
        sb.append(richText.substring(start, richText.length()));
        return sb.toString();
    }

    public static String http2cs(String httpURL) {
        if (httpURL == null || !httpURL.startsWith("http")) {
            return httpURL;
        }

        String id = httpURL;

        int i = httpURL.indexOf("?");
        if (i > 0) {
            id = httpURL.substring(0, i);
        }

        int j = id.lastIndexOf("/");
        if (j > 0) {
            id = id.substring(j + 1, id.length());
        }

        String result = id.replace("_", "/").replace("-", "+");
        int length = result.length();
        if (length % 4 == 2) {
            result += "==";
        } else if (length % 4 == 3) {
            result += "=";
        }
        String md5base64 = new String(Base64.getDecoder().decode(result.getBytes(Charset.forName("utf-8"))));
        String type = md5base64.substring(0, md5base64.indexOf("/") + 1);
        return "cs://1/" + type + id;
    }

    /*public static void main(String[] args) {
        // <p><span style="font-size: 14px;"><img src="||<<<<===>>>>||" style="max-width:100%;"/>保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</span></p><p><span style="font-size: 14px;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style="white-space: normal;">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style="font-size: 14px;"><br/></span><br/></p><p><span style="font-size: 14px;"></span></p><p style="white-space: normal;"><br/></p>
        String rt = "<p><span style=\"font-size: 14px;\"><img src=\"http://alpha-cs.lab.everhomes.com:80/image/aW1hZ2UvTVRwaFlUWXhOR0k0TmpCbU1UTTJaVFkzTnpGbE9UWTNaV1ptTnpabE5UTTFNUQ?token=QpJZg9EOyAWiwpUQdwkptpXkBl73rJ8zonOtMBoYrBUq5BeX6mZBjBxNyeccDbAhJ7uosgA3tPL6PNE9lh4u2OrTVecZ96-DXtj69QPATLHVXofWMSj-I-EKUnoqa7EK\" style=\"max-width:100%;\"/>保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</span></p><p><span style=\"font-size: 14px;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\">保集e智谷所在的上海机器人产业园被划分在张江示范区北园范围内，未来在人才、税收、资金扶持等各方面有望享受一系列政策利好。</p><p style=\"white-space: normal;\">2016年8月10日，保集e智谷被正式授牌为“上海智能制造生产性服务业功能区”，同时也成为上海目前唯一一个以智能制造主题获此殊荣的园区。《上海市制造业转型升级“十三五”规划》中提出“大力发展与制造业紧密相关的生产性服务业，推动生产性服务业向专业化和价值链高端延伸，鼓励发展一批生产性服务业功能区。” 作为对接产业链上下游、集聚重点产业和“四新”经济发展的重要载体，生产性服务业功能区已经成为推动上海产业未来发展的新引擎。</p><p><span style=\"font-size: 14px;\"><br/></span><br/></p><p><span style=\"font-size: 14px;\"></span></p><p style=\"white-space: normal;\"><br/></p>";
        String rt1 = "<p>111111<br/></p><p>fsdf</p><p>sdfds</p><p>fsd</p><p>fsd</p><p>fsd</p><p>fds</p><p><img src=\"http://qh-opv2test-cs.zuolin.com:80/image/aW1hZ2UvTVRvM016TTJOMlEwWmpWallUYzBNMkk0WXpKaVpUazNaR1ZpTnpJMU9ERXlOZw?token=9Dr7c6OTPTG_QCQkcA3fpcnwm5Cs71XeAYj2oYWZrC1BL3XadlOQKybLphZvCow5je_LMI7geEo_B_IYnzhwyjuuhJU_rogFXxww9bu6sBMFatjcF5SRCh4Dt8hrrbvj\" style=\"max-width:100%;\"/></p><p><img src=\"http://qh-opv2test-cs.zuolin.com:80/image/aW1hZ2UvTVRwbU16TXpZMlJsTlRjMk1UUXdaVGN5TldGaE16TTRZVGhrWkRnNE56SmxaUQ?token=9Dr7c6OTPTG_QCQkcA3fpcnwm5Cs71XeAYj2oYWZrC1BL3XadlOQKybLphZvCow5je_LMI7geEo_B_IYnzhwyjuuhJU_rogFXxww9bu6sBMFatjcF5SRCh4Dt8hrrbvj\" style=\"max-width:100%;\"/></p><p>dasfsdfsdfsdf<br/></p><p>fdsfsdf</p><p>fsdf</p><p>sdf</p><p><img src=\"https://www.baidu.com/img/superlogo_c4d7df0a003d3db9b65e9ef0fe6da1ec.png\" style=\"max-width:100%;\"/></p>";
        System.out.println(rt1);
        System.out.println(processImages(rt1, s -> s));


        // cs://1/image/aW1hZ2UvTVRwbU1ETmtOVGc0WmpnME1XWXpNVGN6TXpsaE1UZzBPVFpqTWpVME5qTXdOQQ
        // cs://1/image/aW1hZ2UvTVRvMVlXWTJaalE0TUdGbU5URTNZalF3WmpZeVl6UTJPVEl3Wm1ZeFlqSTNNUQ
        System.out.println(http2cs("https://content-1.zuolin.com:443/file/aW1hZ2UvTVRvMVlXWTJaalE0TUdGbU5URTNZalF3WmpZeVl6UTJPVEl3Wm1ZeFlqSTNNUQ?token=xxx"));
    }*/
}
