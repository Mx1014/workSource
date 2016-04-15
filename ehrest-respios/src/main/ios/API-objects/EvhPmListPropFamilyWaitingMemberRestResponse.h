//
// EvhPmListPropFamilyWaitingMemberRestResponse.h
// generated at 2016-04-12 15:02:21 
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
