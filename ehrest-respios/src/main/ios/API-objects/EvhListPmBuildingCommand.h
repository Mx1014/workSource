//
// EvhListPmBuildingCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmBuildingCommand
//
@interface EvhListPmBuildingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orgId;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

