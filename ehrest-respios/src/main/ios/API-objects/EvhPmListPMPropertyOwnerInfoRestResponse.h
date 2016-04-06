//
// EvhPmListPMPropertyOwnerInfoRestResponse.h
// generated at 2016-04-06 19:10:44 
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
