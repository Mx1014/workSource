//
// EvhPmListPropFamilyWaitingMemberRestResponse.h
// generated at 2016-03-31 19:08:54 
//
#import "RestResponseBase.h"
#import "EvhListPropFamilyWaitingMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPropFamilyWaitingMemberRestResponse
//
@interface EvhPmListPropFamilyWaitingMemberRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPropFamilyWaitingMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
