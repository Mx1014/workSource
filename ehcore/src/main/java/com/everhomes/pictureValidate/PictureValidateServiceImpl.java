// @formatter:off
package com.everhomes.pictureValidate;

import com.everhomes.PictureValidate.PictureValidateService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.pictureValidate.GetNewPictureValidateCodeCommand;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.everhomes.util.RuntimeErrorException.errorWith;

@Component
public class PictureValidateServiceImpl implements PictureValidateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureValidateServiceImpl.class);
    private static Random random = new Random();

    private static String[] allCharacter = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private BigCollectionProvider collectionProvider;

    @Autowired
    private UserProvider userProvider;


    @Override
    public String newPicture(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return this.newPicture(sessionId);
    }

    @Override
    public String newPictureByApp(GetNewPictureValidateCodeCommand cmd) {
        User user = UserContext.current().getUser();
        if (user == null) {
            LOGGER.error("cannot find user");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "cannot find user");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getIdentifier());
        if(userIdentifier==null){
            LOGGER.error("cannot find user identifierToken.identifierToken={}",cmd.getIdentifier());
            throw errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_USER_NOT_EXIST, "can not find user identifierToken or status is error");
        }

        //判断手机号的用户与当前用户是否一致
        if (userIdentifier.getOwnerUid() == null || !userIdentifier.getOwnerUid().equals(UserContext.currentUserId())) {
            LOGGER.error("phone not match user error");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PHONE_NOT_MATCH_USER,
                    "phone not match user error");
        }

        if (cmd.getRegionCode() != null && userIdentifier.getRegionCode() != null && !cmd.getRegionCode().equals(userIdentifier.getRegionCode())) {
            LOGGER.error("phone not match user error");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PHONE_NOT_MATCH_USER,
                    "phone not match user error");
        }
        String sessionId = user.getId().toString();
        return this.newPicture(sessionId);
    }

    @Override
    public String newPicture(String sessionId) {

        int length = Integer.parseInt(configProvider.getValue("picture.validate.length", "6"));
        String code = this.createCode(length);
        String pictureStr = this.createPic(code);

        //测试待删  TODO
//        this.showPic(pictureStr);

        Accessor accessor = this.collectionProvider.getMapAccessor("picturevalidate", "abc");
        RedisTemplate template = accessor.getTemplate(new StringRedisSerializer());
        template.opsForValue().set(sessionId, code, 2*60, TimeUnit.SECONDS);
        return pictureStr;
    }

    @Override
    public Boolean validateCode(HttpServletRequest request, String code) {
        String sessionId = request.getSession().getId();
        return this.validateCode(sessionId, code);
    }

    @Override
    public Boolean validateCodeByApp(String code) {
        User user = UserContext.current().getUser();
        if (user == null) {
            LOGGER.error("cannot find user");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "cannot find user");
        }
        String sessionId = user.getId().toString();
        return this.validateCode(sessionId,code);
    }

    @Override
    public Boolean validateCode(String sessionId, String code) {
        Accessor accessor = this.collectionProvider.getMapAccessor("picturevalidate", "abc");
        RedisTemplate template = accessor.getTemplate(new StringRedisSerializer());
        String saveCode = (String) template.opsForValue().get(sessionId);
        if(code != null && saveCode != null && code.toLowerCase().equals(saveCode.toLowerCase())){
            //清空验证码
            template.opsForValue().getOperations().delete(sessionId);
            return true;
        }

        return false;
    }

    private String createCode(int length) {

        String code = "";
        for (int i = 0; i < length; i++) {

            if (random.nextBoolean()) {
                code += random.nextInt(10);
            } else {
                int m = random.nextInt(52);
                code += allCharacter[m];
            }
        }
        return code;
    }


    private String createPic(String code) {
        try {
            int width = Integer.parseInt(configProvider.getValue("picture.validate.width", "150"));
            int height = Integer.parseInt(configProvider.getValue("picture.validate.height", "50"));
            int colorMin = Integer.parseInt(configProvider.getValue("picture.validate.colorMin", "50"));
            int colorMax = Integer.parseInt(configProvider.getValue("picture.validate.colorMax", "200"));
            int bgColorMin = Integer.parseInt(configProvider.getValue("picture.validate.bgColorMin", "150"));
            int bgColorMax = Integer.parseInt(configProvider.getValue("picture.validate.bgColorMax", "250"));
            int fontSize = Integer.parseInt(configProvider.getValue("picture.validate.fontSize", "30"));

            //生成图片
            BufferedImage localBufferedImage = new BufferedImage(width, height, 1);
            Graphics localGraphics = localBufferedImage.getGraphics();
            localGraphics.setColor(getRandColor(bgColorMin, bgColorMax));
            localGraphics.fillRect(0, 0, width, height);
            localGraphics.setFont(new Font("Comic San MS", 0, fontSize));

            //画随机椭圆
            for (int i = 0; i < 5; ++i) {
                localGraphics.setColor(getRandColor(colorMin, colorMax));
                int l = random.nextInt(width);
                int i1 = random.nextInt(height);
                int i2 = random.nextInt(width / 2);
                int i3 = random.nextInt(height / 2);
                localGraphics.drawOval(l, i1, l + i2, i1 + i3);
            }

            //画随机线
            for (int i = 0; i < 5; i++) {

                localGraphics.setColor(getRandColor(colorMin, colorMax));
                localGraphics.drawLine(rando(0, width), rando(0, height), rando(0, width), rando(0, height));
            }

            //画验证码
            int eachW = (width - 20) / code.length();
            for (int i = 0; i < code.length(); ++i) {
                localGraphics.setColor(getRandColor(colorMin, colorMax));
                char l = code.charAt(i);
                String str = String.valueOf(l);
                localGraphics.drawString(str, rando(eachW * i, eachW * (i + 1)), rando(height / 2, height));
            }

            //将图片转换成二进制
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(localBufferedImage, "png", baos);
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encodeBuffer(baos.toByteArray()).trim();
        } catch (Exception es) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "create picture validate fail. ");
        }
    }

    private Color getRandColor(int min, int max) {

        int r = rando(min, max);

        int g = rando(min, max);

        int b = rando(min, max);

        return new Color(r, g, b);
    }


    private int rando(int min, int max) {
        return random.nextInt(max - min) + min;
    }

/**
 * 后台测试专用
 */
//    public void showPic(String pictureStr) {
//        try {
//
//            BASE64Decoder decoder = new BASE64Decoder();
//
//            byte[] bytes1 = decoder.decodeBuffer(pictureStr);
//
//            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
//            BufferedImage bi1 = ImageIO.read(bais);
//            File w2 = new File("vcode.png");//可以是jpg,png,gif格式
//            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
//        } catch (IOException e) {
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//                    "show picture validate fail. ");
//        }
//    }
}
