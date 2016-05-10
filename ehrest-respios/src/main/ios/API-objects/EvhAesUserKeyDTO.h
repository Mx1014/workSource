//
// EvhAesUserKeyDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAesUserKeyDTO
//
@interface EvhAesUserKeyDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* keyId;

@property(nonatomic, copy) NSNumber* createTimeMs;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* keyType;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* secret;

@property(nonatomic, copy) NSNumber* expireTimeMs;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* hardwareId;

@property(nonatomic, copy) NSString* doorName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

