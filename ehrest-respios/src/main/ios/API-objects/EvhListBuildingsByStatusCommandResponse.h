//
// EvhListBuildingsByStatusCommandResponse.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBuildingsByStatusCommandResponse
//
@interface EvhListBuildingsByStatusCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhCommunityBuildingDTO*
@property(nonatomic, strong) NSMutableArray* buildings;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

