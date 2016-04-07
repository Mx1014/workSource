//
// EvhFamilyListNeighborUsersRestResponse.h
// generated at 2016-04-07 15:16:54 
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
