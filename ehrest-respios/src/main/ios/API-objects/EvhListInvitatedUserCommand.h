//
// EvhListInvitatedUserCommand.h
// generated at 2016-04-06 19:10:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListInvitatedUserCommand
//
@interface EvhListInvitatedUserCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* anchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

