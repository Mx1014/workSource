//
// EvhSetWaitingDaysCommand.h
// generated at 2016-03-25 19:05:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetWaitingDaysCommand
//
@interface EvhSetWaitingDaysCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* days;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

