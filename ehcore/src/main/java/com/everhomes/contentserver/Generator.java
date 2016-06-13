package com.everhomes.contentserver;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.sequence.LocalSequenceGenerator;

public class Generator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);

    public static String createHashKey(String... keys) throws NoSuchAlgorithmException {
        String decript = StringUtils.join(keys, "-");
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(decript.getBytes());
        byte messageDigest[] = digest.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        String value = hexString.toString();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create hash value {}", value);
        return value;
    }

    public static String createRandomKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createKey(Long serverId, String path, String type) {
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }
        return String.format("cs://%s/%s/%s", serverId, type, encodeUrl(path));
    }

    public static String encodeUrl(String path) {
        byte[] code = Base64.getEncoder().encode(path.getBytes(Charset.forName("utf-8")));
        String str = new String(code, Charset.forName("utf-8")).replace("/", "_").replace("+", "-").replace("=", "");
        return str;

    }

    public static String decodeUrl(String path) {
        String result = path.replace("_", "/").replace("-", "+");
        int length = result.length();
        if (length % 4 == 2) {
            result += "==";
        } else if (length % 4 == 3) {
            result += "=";
        }

        return new String(Base64.getDecoder().decode(result.getBytes(Charset.forName("utf-8"))));
    }

    public static int randomInt(int max) {
        return new Random().nextInt(max);
    }

    public static long randomLong() {
        return System.currentTimeMillis();
    }

    public static PduFrame createPduFrame(String name, Object payLoad, long requestId) {
        PduFrame pduFrame = new PduFrame();
        pduFrame.setName(name);
        pduFrame.setPayload(payLoad);
        pduFrame.setRequestId(requestId == 0 ? LocalSequenceGenerator.getNextSequence() : requestId);
        return pduFrame;
    }

    public static String createCacheKey(long uid, String objetId) {
        return String.format("%d-%s", uid, objetId);
    }

}
