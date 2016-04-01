//
// EvhAdminLaunchpadGetLaunchPadItemByIdRestResponse.h
// generated at 2016-04-01 15:40:24 
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
