//
// EvhTechparkPunchListPunchCountRestResponse.h
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
