//
// EvhTechparkPunchListPunchCountRestResponse.h
// generated at 2016-04-07 17:03:18 
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
