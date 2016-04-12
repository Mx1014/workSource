//
// EvhRejectAdminRoleCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRejectAdminRoleCommand
//
@interface EvhRejectAdminRoleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* rejectText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

