//
// EvhLaunchpadGetLaunchPadItemByIdRestResponse.h
// generated at 2016-04-07 17:57:44 
//
#import "RestResponseBase.h"
#import "EvhLaunchPadItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchpadGetLaunchPadItemByIdRestResponse
//
@interface EvhLaunchpadGetLaunchPadItemByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLaunchPadItemDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
