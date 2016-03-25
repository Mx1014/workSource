//
// EvhEnterpriseAddressDTO.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseAddressDTO
//
@interface EvhEnterpriseAddressDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* buildingName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

