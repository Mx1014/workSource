//
// EvhTechparkPunchGetPunchNewExceptionRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhGetPunchNewExceptionCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchGetPunchNewExceptionRestResponse
//
@interface EvhTechparkPunchGetPunchNewExceptionRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetPunchNewExceptionCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
