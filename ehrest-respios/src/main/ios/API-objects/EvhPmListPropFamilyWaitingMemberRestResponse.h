//
// EvhPmListPropFamilyWaitingMemberRestResponse.h
// generated at 2016-04-26 18:22:57 
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
