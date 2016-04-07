//
// EvhGroupListGroupWaitingApprovalsRestResponse.h
// generated at 2016-04-07 17:33:50 
//
#import "RestResponseBase.h"
#import "EvhListGroupWaitingApprovalsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListGroupWaitingApprovalsRestResponse
//
@interface EvhGroupListGroupWaitingApprovalsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListGroupWaitingApprovalsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
