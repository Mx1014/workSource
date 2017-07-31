package com.everhomes.user.base;

import org.jooq.DSLContext;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.user.SignupCommand;
import com.everhomes.rest.user.VerifyAndLogonCommand;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.daos.EhUserIdentifiersDao;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.user.SignupToken;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;

public class UserGenerator extends CoreServerTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGenerator.class);

    @Autowired
    DbProvider dbProvider;

    @Autowired
    UserService userService;

    private String phone;

    private String password;

    // delete data if you wanna todo
    protected long ownId;

    // delete identifier
    protected long identifierId;

    protected long createLoginUser(String number, String password) {
        Assert.assertNotNull(number, "number cannot be empty");
        Assert.assertNotNull(password, "password cannot be empty");
        this.phone = number;
        this.password = password;
        return testLoginWorkflow();
    }

    private long testLoginWorkflow() {
        SignupCommand cmd = new SignupCommand();
        cmd.setToken(phone);
        cmd.setType("mobile");
        try {
            SignupToken rsp = userService.signup(cmd,null);
            DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUserIdentifiers.class));
            EhUserIdentifiersDao dao = new EhUserIdentifiersDao(cxt.configuration());
            com.everhomes.server.schema.tables.pojos.EhUserIdentifiers ret = dao.fetchByIdentifierToken(phone).stream()
                    .findFirst().get();
            identifierId = ret.getId();
            LOGGER.info("----------------------------identifier={}", identifierId);
            VerifyAndLogonCommand verifyCommand = new VerifyAndLogonCommand();
            verifyCommand.setInitialPassword(password);
            verifyCommand.setNamespaceId(0);
            verifyCommand.setSignupToken(WebTokenGenerator.getInstance().toWebToken(rsp));
            verifyCommand.setVerificationCode(ret.getVerificationCode());
            UserLogin verifyRsp = userService.verifyAndLogon(verifyCommand);
            ownId = verifyRsp.getUserId();
            Assert.assertNotEquals(ownId, 0L);
            LOGGER.info("----------------------------ownId={}", ownId);
            // login
            UserLogin login = userService.logon(0, cmd.getRegionCode(), phone, password, null, null);

            LOGGER.info("----------------------------login={}", ownId);
            Assert.assertEquals(login.getUserId(), ownId);
            return ownId;
        } catch (Exception e) {
            LOGGER.error("signup failed");
            fail();
        }
        LOGGER.error("unknown error");
        throw new RuntimeErrorException("test failed");

    }

    // clear data if you want to
    protected void clear() {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserIdentifiers.class));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(cxt.configuration());
        dao.deleteById(identifierId);

        cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class));
        EhUsersDao userDao = new EhUsersDao(cxt.configuration());
        userDao.deleteById(ownId);

    }
}
