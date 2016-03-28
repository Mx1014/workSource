//
// EvhListCommunitiesBySceneResponse.h
// generated at 2016-03-28 15:56:07 
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

