//
// EvhTechparkPunchListPunchExceptionApprovalRestResponse.h
// generated at 2016-03-25 09:26:45 
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
