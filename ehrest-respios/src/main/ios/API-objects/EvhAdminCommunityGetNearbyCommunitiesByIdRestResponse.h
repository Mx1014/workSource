//
// EvhAdminCommunityGetNearbyCommunitiesByIdRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityGetNearbyCommunitiesByIdRestResponse
//
@interface EvhAdminCommunityGetNearbyCommunitiesByIdRestResponse : EvhRestResponseBase

// array of EvhCommunityDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
