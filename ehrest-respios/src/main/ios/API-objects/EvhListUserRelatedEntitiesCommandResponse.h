//
// EvhListUserRelatedEntitiesCommandResponse.h
// generated at 2016-03-25 09:26:39 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyDTO.h"
#import "EvhOrganizationSimpleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserRelatedEntitiesCommandResponse
//
@interface EvhListUserRelatedEntitiesCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhFamilyDTO*
@property(nonatomic, strong) NSMutableArray* familyList;

// item type EvhOrganizationSimpleDTO*
@property(nonatomic, strong) NSMutableArray* organizationList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

