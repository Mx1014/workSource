//
// EvhFamilyListNearbyNeighborUsersRestResponse.h
// generated at 2016-04-07 14:16:31 
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
