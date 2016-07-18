//
// EvhListNewsBySceneResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBriefNewsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNewsBySceneResponse
//
@interface EvhListNewsBySceneResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhBriefNewsDTO*
@property(nonatomic, strong) NSMutableArray* theNewsList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

