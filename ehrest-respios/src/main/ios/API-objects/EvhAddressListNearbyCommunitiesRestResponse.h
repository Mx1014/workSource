//
// EvhAddressListNearbyCommunitiesRestResponse.h
// generated at 2016-03-25 11:43:34 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListNearbyCommunitiesRestResponse
//
@interface EvhAddressListNearbyCommunitiesRestResponse : EvhRestResponseBase

// array of EvhCommunityDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
