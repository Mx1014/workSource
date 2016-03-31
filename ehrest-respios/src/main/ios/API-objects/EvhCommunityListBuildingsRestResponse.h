//
// EvhCommunityListBuildingsRestResponse.h
// generated at 2016-03-28 15:56:09 
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
