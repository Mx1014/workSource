//
// EvhFamilyListNearbyNeighborUsersRestResponse.h
// generated at 2016-03-25 09:26:44 
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
