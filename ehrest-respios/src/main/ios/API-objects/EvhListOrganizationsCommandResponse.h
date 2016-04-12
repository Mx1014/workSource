//
// EvhListOrganizationsCommandResponse.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationsCommandResponse
//
@interface EvhListOrganizationsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhOrganizationDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

