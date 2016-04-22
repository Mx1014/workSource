//
// EvhLaunchpadGetLaunchPadItemByIdRestResponse.h
// generated at 2016-04-22 13:56:50 
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
