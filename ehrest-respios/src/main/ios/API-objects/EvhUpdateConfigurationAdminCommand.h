//
// EvhUpdateConfigurationAdminCommand.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateConfigurationAdminCommand
//
@interface EvhUpdateConfigurationAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* value;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

