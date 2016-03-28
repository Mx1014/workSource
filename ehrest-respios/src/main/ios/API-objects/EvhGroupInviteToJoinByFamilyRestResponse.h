//
// EvhGroupInviteToJoinByFamilyRestResponse.h
// generated at 2016-03-25 19:05:21 
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
