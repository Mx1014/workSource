//
// EvhListContactGroupNamesByEnterpriseIdCommandResponse.h
// generated at 2016-04-08 20:09:23 
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

