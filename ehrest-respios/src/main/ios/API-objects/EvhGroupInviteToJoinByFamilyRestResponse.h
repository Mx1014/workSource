//
// EvhGroupInviteToJoinByFamilyRestResponse.h
// generated at 2016-04-07 15:16:54 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupInviteToJoinByFamilyRestResponse
//
@interface EvhGroupInviteToJoinByFamilyRestResponse : EvhRestResponseBase

// array of EvhCommandResult* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
