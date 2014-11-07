package com.everhomes.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhUserDevicesDao;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.server.schema.tables.pojos.EhUserDevices;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ActionCallback;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.MapReduceCallback;
import com.everhomes.util.PasswordHash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import static com.everhomes.server.schema.Tables.*;

@Component
public class UserServiceProviderImpl implements UserServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private CacheProvider cacheProvider;
    
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    
    private Gson gson;
    
    public UserServiceProviderImpl() {
        gson = new GsonBuilder().create();   
    }
    
    @Override
    public void createUser(User user) {
        long id = this.shardingProvider.allocShardableContentId(EhUsers.class).second();
        user.setId(id);
        user.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.insert(user);
    }
    
    @Override
    public void updateUser(User user) {
        assert(user.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, user.getId().longValue()));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.update(user);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName());
        String cacheKey = String.valueOf(user.getId());
        cacheAccessor.evict(cacheKey);
    }

    @Override
    public void deleteUser(long id) {
        // TODO delete all user references, due to database sharding, those foreign references will be fully taken
        // care of by database
        
        // delete the record itself
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
        EhUsersDao dao = new EhUsersDao(context.configuration());
        dao.deleteById(id);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName());
        String cacheKey = String.valueOf(id);
        cacheAccessor.evict(cacheKey);
    }
    
    @Override
    public User findUserById(final long id) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName());
        String cacheKey = String.valueOf(id);
        User user = cacheAccessor.get(cacheKey, User.class, new ActionCallback<User>() {
            public User doAction() {
                DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, id));
                EhUsersDao dao = new EhUsersDao(context.configuration());
                EhUsers user = dao.findById(id);
                return ConvertHelper.convert(user, User.class);
            }
        });
        return user;
    }
    
    @Override
    public List<UserDevice> listUserDevicesOfUser(long userId) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-devices");
        String cacheKey = String.valueOf(userId);
        
        String jsonDeviceList = cacheAccessor.get(cacheKey, String.class);
        if(jsonDeviceList == null) {
            return gson.fromJson(jsonDeviceList, new TypeToken<ArrayList<UserDevice>>() {}.getType());
        }
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userId));
        List<UserDevice> devices = context.select().from(EH_USER_DEVICES).where(EH_USER_DEVICES.OWNER_UID.eq(userId))
            .fetch().map(new RecordMapper<Record, UserDevice>() {
               public UserDevice map(Record record) {
                   return ConvertHelper.convert(record, UserDevice.class);
               }
            });
        cacheAccessor.put(cacheKey, gson.toJson(devices));
        return devices;
    }
    
    @Override
    public void createDevice(UserDevice userDevice) {
        assert(userDevice.getOwnerUid() != null);
        
        // device record will be saved in the same shard as its owner users
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userDevice.getOwnerUid().longValue()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserDevices.class));
        
        userDevice.setId(id);
        Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
        userDevice.setCreateTime(ts);
        
        EhUserDevicesDao dao = new EhUserDevicesDao(context.configuration());
        dao.insert(userDevice);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-devices");
        String cacheKey = String.valueOf(userDevice.getOwnerUid());
        cacheAccessor.evict(cacheKey);
    }
    
    @Override
    public void updateDevice(UserDevice userDevice) {
        assert(userDevice.getId() != null);
        assert(userDevice.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userDevice.getOwnerUid().longValue()));
        EhUserDevicesDao dao = new EhUserDevicesDao(context.configuration());
        dao.update(userDevice);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(UserDevice.class.getSimpleName());
        cacheAccessor.evict(userDevice.getDeviceNumber());
        
        cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-devices");
        String cacheKey = String.valueOf(userDevice.getOwnerUid());
        cacheAccessor.evict(cacheKey);
    }

    @Override
    public void deleteDevice(UserDevice userDevice) {
        assert(userDevice.getId() != null);
        assert(userDevice.getOwnerUid() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, userDevice.getOwnerUid().longValue()));
        EhUserDevicesDao dao = new EhUserDevicesDao(context.configuration());
        dao.delete(userDevice);
        
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(UserDevice.class.getSimpleName());
        cacheAccessor.evict(userDevice.getDeviceNumber());
        
        cacheAccessor = this.cacheProvider.getCacheAccessor(User.class.getSimpleName() + "-devices");
        String cacheKey = String.valueOf(userDevice.getOwnerUid());
        cacheAccessor.evict(cacheKey);
    }
    
    @Override
    public void deleteDevice(long id) {
        UserDevice userDevice = findDeviceById(id);
        if(userDevice != null)
            deleteDevice(userDevice);
    }
    
    @Override
    public UserDevice findDeviceById(final long id) {
        final UserDevice[] result = new UserDevice[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, new MapReduceCallback<DSLContext, Object>() {
            @Override
            public boolean map(DSLContext context, Object reducingContext) {
                EhUserDevicesDao dao = new EhUserDevicesDao(context.configuration());
                EhUserDevices userDevice = dao.findById(id);
                if(userDevice != null) {
                    result[0] = ConvertHelper.convert(userDevice, UserDevice.class);
                    return false;
                }
                return true;
            }
        });

        return result[0];
    }
    
    @Override
    public UserDevice findDeviceByDeviceNumberOrEmail(final String deviceNumber) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor(UserDevice.class.getSimpleName());
        UserDevice userDevice = cacheAccessor.get(deviceNumber, UserDevice.class, new ActionCallback<UserDevice>() {
            public UserDevice doAction() {
                final UserDevice[] result = new UserDevice[1];
                
                dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, new MapReduceCallback<DSLContext, Object>() {
                    @Override
                    public boolean map(DSLContext context, Object reducingContext) {
                        Record record = context.select().from(EH_USER_DEVICES)
                            .where(EH_USER_DEVICES.DEVICE_NUMBER.eq(deviceNumber)).fetchOne();
                        
                        if(record != null) {
                            result[0] = ConvertHelper.convert(record, UserDevice.class);
                            return false;
                        }
                        return true;
                    }
                });
                return result[0];
            }
        });
        
        return userDevice;
    }
    
    @Override
    public DeviceLogin logon(String deviceNumberOrEmail, String password) {
        UserDevice userDevice = findDeviceByDeviceNumberOrEmail(deviceNumberOrEmail);
        if(userDevice == null) {
            LOGGER.error("Unable to find device record of " + deviceNumberOrEmail);
            return null;
        }
        
        if(userDevice.getVerificationStatus() != DeviceVerificationStatus.verified.ordinal()) {
            LOGGER.error("Device " + deviceNumberOrEmail + " has not been verified yet");
            return null;
        }
        
        User user = this.findUserById(userDevice.getOwnerUid());
        if(user == null) {
            LOGGER.error("Unable to owner user of device record of " + deviceNumberOrEmail);
            return null;
        }
        
        try {
            if(!PasswordHash.validatePassword(password, user.getPasswordHash())) {
                LOGGER.error("Password does not match for " + deviceNumberOrEmail);
                return null;
            }
        } catch (NoSuchAlgorithmException e) {
            assert(false);
            return null;
        } catch (InvalidKeySpecException e) {
            assert(false);
            return null;
        }
        
        DeviceLogin deviceLogin = new DeviceLogin(userDevice);
        String userKey = NameMapper.getCacheKey("user", userDevice.getOwnerUid(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(userDevice.getId()));
        accessor.putMapValueObject(String.valueOf(userDevice.getId()), deviceLogin, DeviceLogin.class);
        
        return deviceLogin;
    }
    
    @Override
    public boolean isValidLoginToken(LoginToken loginToken) {
        assert(loginToken != null);
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getDeviceId()));
        DeviceLogin deviceLogin = accessor.getMapValueObject(String.valueOf(loginToken.getDeviceId()), DeviceLogin.class);
        if(deviceLogin != null && deviceLogin.getLoginInstanceNumber() == loginToken.getDeviceLoginInstanceNumber())
            return true;
        
        return false;
    }
}
