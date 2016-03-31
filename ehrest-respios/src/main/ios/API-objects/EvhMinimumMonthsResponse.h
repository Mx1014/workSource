//
// EvhMinimumMonthsResponse.h
// generated at 2016-03-31 15:43:22 
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

