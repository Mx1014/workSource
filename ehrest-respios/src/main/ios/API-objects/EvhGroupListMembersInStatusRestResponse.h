//
// EvhGroupListMembersInStatusRestResponse.h
// generated at 2016-04-05 13:45:27 
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
