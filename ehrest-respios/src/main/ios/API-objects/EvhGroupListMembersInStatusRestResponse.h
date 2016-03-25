//
// EvhGroupListMembersInStatusRestResponse.h
// generated at 2016-03-25 09:26:44 
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
