//
// EvhTechparkPunchListPunchExceptionRequestRestResponse.h
// generated at 2016-03-25 17:08:13 
//
#import "RestResponseBase.h"
#import "EvhListPunchExceptionRequestCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchListPunchExceptionRequestRestResponse
//
@interface EvhTechparkPunchListPunchExceptionRequestRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPunchExceptionRequestCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
