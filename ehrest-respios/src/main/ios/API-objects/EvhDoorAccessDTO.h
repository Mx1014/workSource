//
// EvhDoorAccessDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessDTO
//
@interface EvhDoorAccessDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* activeUserId;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* creatorUserId;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* doorType;

@property(nonatomic, copy) NSString* geohash;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* role;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* hardwareId;

@property(nonatomic, copy) NSString* creatorName;

@property(nonatomic, copy) NSString* creatorPhone;

@property(nonatomic, copy) NSNumber* linkStatus;

@property(nonatomic, copy) NSString* version;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

