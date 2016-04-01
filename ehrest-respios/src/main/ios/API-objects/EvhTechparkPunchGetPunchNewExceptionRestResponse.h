//
// EvhTechparkPunchGetPunchNewExceptionRestResponse.h
// generated at 2016-04-01 15:40:24 
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
