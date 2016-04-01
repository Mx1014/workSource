//
// EvhListContactGroupNamesByEnterpriseIdCommandResponse.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactGroupNamesByEnterpriseIdCommandResponse
//
@interface EvhListContactGroupNamesByEnterpriseIdCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhEnterpriseContactDTO*
@property(nonatomic, strong) NSMutableArray* contactGroups;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

