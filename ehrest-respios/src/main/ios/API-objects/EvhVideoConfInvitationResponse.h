//
// EvhVideoConfInvitationResponse.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfInvitationResponse
//
@interface EvhVideoConfInvitationResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* body;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

