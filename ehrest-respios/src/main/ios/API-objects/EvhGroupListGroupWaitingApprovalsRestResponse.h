//
// EvhGroupListGroupWaitingApprovalsRestResponse.h
// generated at 2016-04-19 12:41:55 
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
