//
// EvhCommunityGetBuildingRestResponse.h
// generated at 2016-03-31 13:49:15 
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
