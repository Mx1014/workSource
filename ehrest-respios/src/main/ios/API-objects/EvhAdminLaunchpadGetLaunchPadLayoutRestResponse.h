//
// EvhAdminLaunchpadGetLaunchPadLayoutRestResponse.h
// generated at 2016-04-19 13:40:01 
//
#import "RestResponseBase.h"
#import "EvhLaunchPadLayoutDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminLaunchpadGetLaunchPadLayoutRestResponse
//
@interface EvhAdminLaunchpadGetLaunchPadLayoutRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLaunchPadLayoutDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
