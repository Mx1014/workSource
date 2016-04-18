//
// EvhAdminLaunchpadGetLaunchPadItemByIdRestResponse.h
// generated at 2016-04-18 14:48:52 
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
