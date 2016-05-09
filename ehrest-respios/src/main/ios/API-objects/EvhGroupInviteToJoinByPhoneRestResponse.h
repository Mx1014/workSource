//
// EvhGroupInviteToJoinByPhoneRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupInviteToJoinByPhoneRestResponse
//
@interface EvhGroupInviteToJoinByPhoneRestResponse : EvhRestResponseBase

// array of EvhCommandResult* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
