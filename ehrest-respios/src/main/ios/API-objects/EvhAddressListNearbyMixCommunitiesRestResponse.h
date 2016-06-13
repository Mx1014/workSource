//
// EvhAddressListNearbyMixCommunitiesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListNearbyMixCommunitiesCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListNearbyMixCommunitiesRestResponse
//
@interface EvhAddressListNearbyMixCommunitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNearbyMixCommunitiesCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
