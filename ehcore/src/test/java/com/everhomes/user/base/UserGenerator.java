package com.everhomes.user.base;

import static org.junit.Assert.fail;

import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.daos.EhUserIdentifiersDao;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.user.SignupCommand;
import com.everhomes.user.SignupToken;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.user.VerifyAndLogonCommand;
import com.everhomes.util.RuntimeErrorException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class }, loader = AnnotationConfigContextLoader.class)
public class UserGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGenerator.class);

    @Autowired
    DbProvider dbProvider;

    @Autowired
    UserService userService;

    private String phone;

    private String password;

    // delete data when you wanna todo
    protected long ownId;

    // delete identifier
    protected long identifierId;

    @Configuration
    @ComponentScan(basePackages = { "com.everhomes" })
    @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, })
    static class ContextConfiguration {
    }

    protected long createLoginUser(String number, String password) {
        this.phone = number;
        this.password = password;
        return testLoginWorkflow();
    }

    private long testLoginWorkflow() {
        SignupCommand cmd = new SignupCommand();
        cmd.setToken(phone);
        cmd.setType("mobile");
        try {
            SignupToken rsp = userService.signup(cmd);
            DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUserIdentifiers.class));
            EhUserIdentifiersDao dao = new EhUserIdentifiersDao(cxt.configuration());
            com.everhomes.server.schema.tables.pojos.EhUserIdentifiers ret = dao.fetchByIdentifierToken(phone).stream()
                    .findFirst().get();
            identifierId = ret.getId();
            LOGGER.info("----------------------------identifier={}", identifierId);
            VerifyAndLogonCommand verifyCommand = new VerifyAndLogonCommand();
            verifyCommand.setInitialPassword(password);
            verifyCommand.setNamespaceId(0);
            verifyCommand.setSignupToken(rsp.getTokenString());
            verifyCommand.setVerificationCode(ret.getVerificationCode());
            UserLogin verifyRsp = userService.verifyAndLogon(verifyCommand);
            ownId = verifyRsp.getUserId();
            Assert.assertNotEquals(ownId, 0L);
            LOGGER.info("----------------------------ownId={}", ownId);
            // login
            UserLogin login = userService.logon(0, phone, password, null);

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

    // clear data if you wan to
    protected void clear() {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserIdentifiers.class));
        EhUserIdentifiersDao dao = new EhUserIdentifiersDao(cxt.configuration());
        dao.deleteById(identifierId);

        cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class));
        EhUsersDao userDao = new EhUsersDao(cxt.configuration());
        userDao.deleteById(ownId);

    }
}
