//
// EvhListVestResponse.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVestResponse
//
@interface EvhListVestResponse
    : NSObject<EvhJsonSerializable>


// item type EvhUserInfo*
@property(nonatomic, strong) NSMutableArray* values;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

