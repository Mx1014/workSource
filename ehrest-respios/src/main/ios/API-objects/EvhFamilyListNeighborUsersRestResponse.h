//
// EvhFamilyListNeighborUsersRestResponse.h
// generated at 2016-04-19 12:41:55 
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
