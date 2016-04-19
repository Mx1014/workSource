//
// EvhTechparkPunchListPunchExceptionApprovalRestResponse.h
// generated at 2016-04-19 14:25:58 
//
#import "RestResponseBase.h"
#import "EvhListPunchExceptionRequestCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchListPunchExceptionApprovalRestResponse
//
@interface EvhTechparkPunchListPunchExceptionApprovalRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPunchExceptionRequestCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
