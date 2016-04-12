//
// EvhGroupListGroupWaitingApprovalsRestResponse.h
// generated at 2016-04-08 20:09:23 
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
