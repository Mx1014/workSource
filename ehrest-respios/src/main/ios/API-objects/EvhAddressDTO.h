//
// EvhAddressDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressDTO
//
@interface EvhAddressDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* zipcode;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* geohash;

@property(nonatomic, copy) NSString* addressAlias;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* buildingAliasName;

@property(nonatomic, copy) NSString* apartmentName;

@property(nonatomic, copy) NSString* apartmentFloor;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* deleteTime;

@property(nonatomic, copy) NSNumber* memberStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

