//
// EvhDeleteWarningContactorCommand.h
// generated at 2016-04-12 19:00:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteWarningContactorCommand
//
@interface EvhDeleteWarningContactorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* warningContactorId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

