//
// EvhPmListPMAddressMappingRestResponse.h
// generated at 2016-03-30 10:13:09 
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
