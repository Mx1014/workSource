//
// EvhAddPmBuildingCommand.h
// generated at 2016-04-22 13:56:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddPmBuildingCommand
//
@interface EvhAddPmBuildingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* isAll;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* organizationId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* buildingIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

