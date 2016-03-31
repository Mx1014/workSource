//
// EvhFamilyListNeighborUsersRestResponse.h
// generated at 2016-03-28 15:56:09 
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
