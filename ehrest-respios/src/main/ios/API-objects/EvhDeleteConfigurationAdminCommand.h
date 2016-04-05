//
// EvhDeleteConfigurationAdminCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteConfigurationAdminCommand
//
@interface EvhDeleteConfigurationAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

