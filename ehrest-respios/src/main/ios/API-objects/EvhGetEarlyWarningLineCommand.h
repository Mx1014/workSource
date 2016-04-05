//
// EvhGetEarlyWarningLineCommand.h
// generated at 2016-04-05 13:45:26 
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

