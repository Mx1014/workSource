//
// EvhVideoConfInvitationResponse.h
// generated at 2016-04-26 18:22:54 
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

