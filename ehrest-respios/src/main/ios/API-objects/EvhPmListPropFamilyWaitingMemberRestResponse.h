//
// EvhPmListPropFamilyWaitingMemberRestResponse.h
// generated at 2016-03-30 10:13:09 
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
