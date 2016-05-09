//
// EvhSetMinimumMonthsCommand.h
// generated at 2016-04-29 18:56:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetMinimumMonthsCommand
//
@interface EvhSetMinimumMonthsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* months;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

