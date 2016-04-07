//
// EvhWaitingDaysResponse.h
// generated at 2016-04-07 17:33:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWaitingDaysResponse
//
@interface EvhWaitingDaysResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* waitingDays;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

