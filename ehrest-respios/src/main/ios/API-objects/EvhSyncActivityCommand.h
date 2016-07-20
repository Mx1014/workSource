//
// EvhSyncActivityCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncActivityCommand
//
@interface EvhSyncActivityCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* activityType;

@property(nonatomic, copy) NSNumber* appVersionCode;

@property(nonatomic, copy) NSString* appVersionName;

@property(nonatomic, copy) NSNumber* channelId;

@property(nonatomic, copy) NSString* imeiNumber;

@property(nonatomic, copy) NSString* deviceType;

@property(nonatomic, copy) NSString* osInfo;

@property(nonatomic, copy) NSString* osType;

@property(nonatomic, copy) NSNumber* mktDataVersion;

@property(nonatomic, copy) NSNumber* reportConfigVersion;

@property(nonatomic, copy) NSString* internalIp;

@property(nonatomic, copy) NSNumber* collectTimeMillis;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

