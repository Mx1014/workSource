//
// EvhPmListPMAddressMappingRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhListPropAddressMappingCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPMAddressMappingRestResponse
//
@interface EvhPmListPMAddressMappingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPropAddressMappingCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
