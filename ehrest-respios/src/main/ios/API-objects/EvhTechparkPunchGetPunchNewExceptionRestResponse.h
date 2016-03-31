//
// EvhTechparkPunchGetPunchNewExceptionRestResponse.h
// generated at 2016-03-31 13:49:15 
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
