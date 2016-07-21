//
// EvhBorderListResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBorderListResponse
//
@interface EvhBorderListResponse
    : NSObject<EvhJsonSerializable>


// item type NSString*
@property(nonatomic, strong) NSMutableArray* borders;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

