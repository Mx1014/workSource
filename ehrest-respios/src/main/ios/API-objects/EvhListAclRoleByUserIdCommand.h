//
// EvhListAclRoleByUserIdCommand.h
// generated at 2016-03-25 09:26:39 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAclRoleByUserIdCommand
//
@interface EvhListAclRoleByUserIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

