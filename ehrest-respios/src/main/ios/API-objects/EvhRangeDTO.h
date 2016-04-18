//
// EvhRangeDTO.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhTimeRangeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRangeDTO
//
@interface EvhRangeDTO
    : NSObject<EvhJsonSerializable>


// item type EvhTimeRangeDTO*
@property(nonatomic, strong) NSMutableArray* ranges;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

