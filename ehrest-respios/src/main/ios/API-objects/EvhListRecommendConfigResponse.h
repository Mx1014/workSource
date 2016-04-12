//
// EvhListRecommendConfigResponse.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRecommendConfigDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRecommendConfigResponse
//
@interface EvhListRecommendConfigResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhRecommendConfigDTO*
@property(nonatomic, strong) NSMutableArray* recommendConfigs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

