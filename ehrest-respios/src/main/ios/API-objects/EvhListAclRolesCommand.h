//
// EvhListAclRolesCommand.h
// generated at 2016-03-31 11:07:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAclRolesCommand
//
@interface EvhListAclRolesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

