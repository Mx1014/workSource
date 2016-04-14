//
// EvhSetEarlyWarningLineCommand.h
// generated at 2016-04-12 19:00:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetEarlyWarningLineCommand
//
@interface EvhSetEarlyWarningLineCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* warningLine;

@property(nonatomic, copy) NSNumber* warningLineType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

