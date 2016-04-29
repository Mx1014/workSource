//
// EvhAesUserKeyDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

