//
// EvhListPmBuildingCommandResponse.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmBuildingCommandResponse
//
@interface EvhListPmBuildingCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhPmBuildingDTO*
@property(nonatomic, strong) NSMutableArray* pmBuildings;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

