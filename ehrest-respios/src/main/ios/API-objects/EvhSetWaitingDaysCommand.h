//
// EvhSetWaitingDaysCommand.h
// generated at 2016-04-22 13:56:45 
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

