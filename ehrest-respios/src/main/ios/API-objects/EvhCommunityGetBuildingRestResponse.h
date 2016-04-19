//
// EvhCommunityGetBuildingRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhCommunityBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityGetBuildingRestResponse
//
@interface EvhCommunityGetBuildingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityBuildingDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
