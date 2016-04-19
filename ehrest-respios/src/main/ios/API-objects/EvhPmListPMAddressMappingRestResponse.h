//
// EvhPmListPMAddressMappingRestResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:58 
>>>>>>> 3.3.x
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
