//
// EvhSetWaitingDaysCommand.h
// generated at 2016-03-30 10:13:08 
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

