//
// EvhWaitingDaysResponse.h
// generated at 2016-03-25 15:57:22 
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

