//
// EvhPmListPMGroupMembersRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhListPropMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPMGroupMembersRestResponse
//
@interface EvhPmListPMGroupMembersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPropMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
