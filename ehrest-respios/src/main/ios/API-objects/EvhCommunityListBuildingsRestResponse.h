//
// EvhCommunityListBuildingsRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhListBuildingCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityListBuildingsRestResponse
//
@interface EvhCommunityListBuildingsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListBuildingCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
