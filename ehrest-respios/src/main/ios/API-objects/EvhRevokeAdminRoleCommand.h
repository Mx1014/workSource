//
// EvhRevokeAdminRoleCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRevokeAdminRoleCommand
//
@interface EvhRevokeAdminRoleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* revokeText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

