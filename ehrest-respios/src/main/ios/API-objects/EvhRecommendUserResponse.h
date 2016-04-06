//
// EvhRecommendUserResponse.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRecommendUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendUserResponse
//
@interface EvhRecommendUserResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRecommendUserInfo*
@property(nonatomic, strong) NSMutableArray* users;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

