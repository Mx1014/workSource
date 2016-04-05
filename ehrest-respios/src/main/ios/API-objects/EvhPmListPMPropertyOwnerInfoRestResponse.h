//
// EvhPmListPMPropertyOwnerInfoRestResponse.h
// generated at 2016-04-05 13:45:27 
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
