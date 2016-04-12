//
// EvhPmListPMPropertyOwnerInfoRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhListPropOwnerCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPMPropertyOwnerInfoRestResponse
//
@interface EvhPmListPMPropertyOwnerInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPropOwnerCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
