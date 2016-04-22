//
// EvhCreateVideoConfInvitationCommand.h
// generated at 2016-04-22 13:56:46 
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

