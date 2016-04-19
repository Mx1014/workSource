//
// EvhRecommendConfigResponse.h
// generated at 2016-04-19 14:25:56 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRecommendConfigDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendConfigResponse
//
@interface EvhRecommendConfigResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhRecommendConfigDTO*
@property(nonatomic, strong) NSMutableArray* configs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

