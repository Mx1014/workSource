//
// EvhMinimumMonthsResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMinimumMonthsResponse
//
@interface EvhMinimumMonthsResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* months;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

