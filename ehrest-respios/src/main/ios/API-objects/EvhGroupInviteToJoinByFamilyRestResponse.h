//
// EvhGroupInviteToJoinByFamilyRestResponse.h
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
