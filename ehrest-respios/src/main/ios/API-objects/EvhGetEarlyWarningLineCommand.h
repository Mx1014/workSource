//
// EvhGetEarlyWarningLineCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEarlyWarningLineCommand
//
@interface EvhGetEarlyWarningLineCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* warningLineType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

