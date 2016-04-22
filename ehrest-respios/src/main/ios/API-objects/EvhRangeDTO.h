//
// EvhRangeDTO.h
// generated at 2016-04-22 13:56:46 
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

