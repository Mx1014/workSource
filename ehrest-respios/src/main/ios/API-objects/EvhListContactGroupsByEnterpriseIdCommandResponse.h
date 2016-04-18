//
// EvhListContactGroupsByEnterpriseIdCommandResponse.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactGroupsByEnterpriseIdCommandResponse
//
@interface EvhListContactGroupsByEnterpriseIdCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseContactDTO*
@property(nonatomic, strong) NSMutableArray* contactGroups;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

