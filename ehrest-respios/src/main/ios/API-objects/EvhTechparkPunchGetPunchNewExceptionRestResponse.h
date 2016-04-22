//
// EvhTechparkPunchGetPunchNewExceptionRestResponse.h
// generated at 2016-04-22 13:56:51 
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
