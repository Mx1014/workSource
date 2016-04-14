//
// EvhAdminLaunchpadGetLaunchPadItemByIdRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhLaunchPadItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminLaunchpadGetLaunchPadItemByIdRestResponse
//
@interface EvhAdminLaunchpadGetLaunchPadItemByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLaunchPadItemDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
