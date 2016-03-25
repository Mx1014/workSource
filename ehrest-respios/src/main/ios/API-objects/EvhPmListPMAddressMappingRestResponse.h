//
// EvhPmListPMAddressMappingRestResponse.h
// generated at 2016-03-25 15:57:24 
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
