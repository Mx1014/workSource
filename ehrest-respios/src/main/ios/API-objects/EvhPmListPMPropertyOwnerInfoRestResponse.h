//
// EvhPmListPMPropertyOwnerInfoRestResponse.h
// generated at 2016-04-22 13:56:51 
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
