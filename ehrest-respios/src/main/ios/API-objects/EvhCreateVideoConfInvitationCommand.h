//
// EvhCreateVideoConfInvitationCommand.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateVideoConfInvitationCommand
//
@interface EvhCreateVideoConfInvitationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* reserveConfId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

