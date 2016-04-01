//
// EvhPmListPropFamilyWaitingMemberRestResponse.h
// generated at 2016-04-01 15:40:24 
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
