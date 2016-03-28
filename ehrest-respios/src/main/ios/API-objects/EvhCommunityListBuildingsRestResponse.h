//
// EvhCommunityListBuildingsRestResponse.h
// generated at 2016-03-25 19:05:21 
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
