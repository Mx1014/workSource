//
// EvhGetUserPointResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

