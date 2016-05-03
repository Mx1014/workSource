//
// EvhFamilyListNearbyNeighborUsersRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyListNearbyNeighborUsersRestResponse
//
@interface EvhFamilyListNearbyNeighborUsersRestResponse : EvhRestResponseBase

// array of EvhNeighborUserDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
