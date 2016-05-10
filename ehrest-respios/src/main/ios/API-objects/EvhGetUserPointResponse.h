//
// EvhGetUserPointResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserScoreDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserPointResponse
//
@interface EvhGetUserPointResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* uid;

// item type EvhUserScoreDTO*
@property(nonatomic, strong) NSMutableArray* userPoints;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

