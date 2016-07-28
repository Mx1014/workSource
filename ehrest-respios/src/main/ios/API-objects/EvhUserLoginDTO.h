//
// EvhUserLoginDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserLoginDTO
//
@interface EvhUserLoginDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* loginId;

@property(nonatomic, copy) NSString* deviceIdentifier;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* loginBorderId;

@property(nonatomic, copy) NSNumber* loginInstanceNumber;

@property(nonatomic, copy) NSNumber* lastAccessTick;

@property(nonatomic, copy) NSNumber* portalRole;

@property(nonatomic, copy) NSNumber* partnerId;

@property(nonatomic, copy) NSString* pusherIdentify;

@property(nonatomic, copy) NSNumber* isOnline;

@property(nonatomic, copy) NSString* deviceType;

@property(nonatomic, copy) NSString* borderStatus;

@property(nonatomic, copy) NSString* borderSessionId;

@property(nonatomic, copy) NSNumber* lastPushPing;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

