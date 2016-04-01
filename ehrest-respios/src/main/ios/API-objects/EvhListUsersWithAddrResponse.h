//
// EvhListUsersWithAddrResponse.h
// generated at 2016-03-31 20:15:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUsersWithAddrResponse
//
@interface EvhListUsersWithAddrResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* cellPhone;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* apartmentName;

@property(nonatomic, copy) NSNumber* addressStatus;

@property(nonatomic, copy) NSNumber* apartmentStatus;

@property(nonatomic, copy) NSString* familyName;

@property(nonatomic, copy) NSString* cellPhoneNumberLocation;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

