//
// EvhAssumePortalRoleCommand.h
// generated at 2016-04-22 13:56:45 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAssumePortalRoleCommand
//
@interface EvhAssumePortalRoleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* roleId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

