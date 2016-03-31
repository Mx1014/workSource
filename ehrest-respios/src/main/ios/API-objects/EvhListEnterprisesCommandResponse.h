//
// EvhListEnterprisesCommandResponse.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterprisesCommandResponse
//
@interface EvhListEnterprisesCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhOrganizationDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

