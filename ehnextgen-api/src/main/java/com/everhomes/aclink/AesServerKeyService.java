package com.everhomes.aclink;

public interface AesServerKeyService {

    Integer getAckingSecretVersion(DoorAccess doorAccess);

    Integer getExpectSecretKey(DoorAccess doorAccess);

    Long createAesServerKey(AesServerKey obj);

    AesServerKey getCurrentAesServerKey(Long doorAccessId);

}
