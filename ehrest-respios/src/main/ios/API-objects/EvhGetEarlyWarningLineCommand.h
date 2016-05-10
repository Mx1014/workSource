//
// EvhGetEarlyWarningLineCommand.h
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

