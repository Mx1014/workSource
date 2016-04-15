//
// EvhWaitingDaysResponse.h
// generated at 2016-04-12 15:02:18 
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

