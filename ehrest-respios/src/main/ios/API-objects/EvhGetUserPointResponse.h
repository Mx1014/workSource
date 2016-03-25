//
// EvhGetUserPointResponse.h
// generated at 2016-03-25 15:57:21 
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

