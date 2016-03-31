//
// EvhGroupInviteToJoinByPhoneRestResponse.h
// generated at 2016-03-31 13:49:15 
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
