//
// EvhDoorAuthDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAuthDTO
//
@interface EvhDoorAuthDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* authType;

@property(nonatomic, copy) NSNumber* validFromMs;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* validEndMs;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* approveUserId;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSString* doorName;

@property(nonatomic, copy) NSString* hardwareId;

@property(nonatomic, copy) NSString* nickname;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSString* approveUserName;

@property(nonatomic, copy) NSString* organization;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* address;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

