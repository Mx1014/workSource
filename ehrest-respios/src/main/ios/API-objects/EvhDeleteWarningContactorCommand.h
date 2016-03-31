//
// EvhDeleteWarningContactorCommand.h
// generated at 2016-03-28 15:56:08 
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

