//
// EvhTechparkPunchListPunchCountRestResponse.h
// generated at 2016-03-30 10:13:09 
//
#import "RestResponseBase.h"
#import "EvhListPunchCountCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchListPunchCountRestResponse
//
@interface EvhTechparkPunchListPunchCountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPunchCountCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
