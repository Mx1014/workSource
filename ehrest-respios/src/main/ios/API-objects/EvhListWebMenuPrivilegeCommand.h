//
// EvhListWebMenuPrivilegeCommand.h
// generated at 2016-04-22 13:56:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuPrivilegeCommand
//
@interface EvhListWebMenuPrivilegeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

