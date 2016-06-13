//
// EvhCommunityStatusResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhContact.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityStatusResponse
//
@interface EvhCommunityStatusResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userCount;

// item type EvhContact*
@property(nonatomic, strong) NSMutableArray* contacts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

