//
// EvhGetDayPunchLogsCommand.h
// generated at 2016-03-31 10:18:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetDayPunchLogsCommand
//
@interface EvhGetDayPunchLogsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpirseId;

@property(nonatomic, copy) NSString* queryDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

