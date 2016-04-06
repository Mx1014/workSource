//
// EvhTechparkPunchListPunchExceptionRequestRestResponse.h
// generated at 2016-04-06 19:10:44 
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
