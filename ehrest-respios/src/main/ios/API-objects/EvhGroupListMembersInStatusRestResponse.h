//
// EvhGroupListMembersInStatusRestResponse.h
// generated at 2016-04-22 13:56:50 
//
#import "RestResponseBase.h"
#import "EvhListMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListMembersInStatusRestResponse
//
@interface EvhGroupListMembersInStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
