//
// EvhGetUserRelatedAddressResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyDTO.h"
#import "EvhOrganizationDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserRelatedAddressResponse
//
@interface EvhGetUserRelatedAddressResponse
    : NSObject<EvhJsonSerializable>


// item type EvhFamilyDTO*
@property(nonatomic, strong) NSMutableArray* familyList;

// item type EvhOrganizationDetailDTO*
@property(nonatomic, strong) NSMutableArray* organizationList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

