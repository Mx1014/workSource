//
// EvhOrganizationDTO2.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationDTO2
//
@interface EvhOrganizationDTO2
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orgId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* communityIds;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* tokenList;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* orgName;

@property(nonatomic, copy) NSString* communityNames;

@property(nonatomic, copy) NSString* addressName;

@property(nonatomic, copy) NSString* orgType;

@property(nonatomic, copy) NSString* tokens;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* areaName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

