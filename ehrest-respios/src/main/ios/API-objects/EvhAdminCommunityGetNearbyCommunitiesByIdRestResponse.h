//
// EvhAdminCommunityGetNearbyCommunitiesByIdRestResponse.h
// generated at 2016-03-25 11:43:34 
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
