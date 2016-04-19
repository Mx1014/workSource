//
// EvhGetUserRelatedAddressResponse.h
// generated at 2016-04-19 12:41:53 
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

