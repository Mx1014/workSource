//
// EvhCommunityStatusResponse.h
// generated at 2016-03-25 15:57:21 
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

