//
// EvhCommunityGetBuildingRestResponse.h
// generated at 2016-04-19 14:25:57 
//
#import "RestResponseBase.h"
#import "EvhBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityGetBuildingRestResponse
//
@interface EvhCommunityGetBuildingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBuildingDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
