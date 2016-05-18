//
// EvhFamilyListNeighborUsersRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListNeighborUsersCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyListNeighborUsersRestResponse
//
@interface EvhFamilyListNeighborUsersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNeighborUsersCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
