//
// EvhPmListPMPropertyOwnerInfoRestResponse.h
// generated at 2016-03-25 11:43:35 
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
