//
// EvhListCommunitiesBySceneResponse.h
// generated at 2016-04-22 13:56:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCommunitiesBySceneResponse
//
@interface EvhListCommunitiesBySceneResponse
    : NSObject<EvhJsonSerializable>


// item type EvhCommunityDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

