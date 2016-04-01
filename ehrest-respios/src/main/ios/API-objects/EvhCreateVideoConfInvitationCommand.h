//
// EvhCreateVideoConfInvitationCommand.h
// generated at 2016-03-31 20:15:30 
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

