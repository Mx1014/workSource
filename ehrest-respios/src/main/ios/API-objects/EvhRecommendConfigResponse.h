//
// EvhRecommendConfigResponse.h
// generated at 2016-03-28 15:56:08 
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

