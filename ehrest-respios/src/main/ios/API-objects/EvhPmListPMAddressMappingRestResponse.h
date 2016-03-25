//
// EvhPmListPMAddressMappingRestResponse.h
// generated at 2016-03-25 11:43:35 
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
