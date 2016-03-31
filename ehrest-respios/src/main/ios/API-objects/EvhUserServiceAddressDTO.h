//
// EvhUserServiceAddressDTO.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserServiceAddressDTO
//
@interface EvhUserServiceAddressDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* province;

@property(nonatomic, copy) NSString* city;

@property(nonatomic, copy) NSString* area;

@property(nonatomic, copy) NSString* callPhone;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSNumber* addressType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

