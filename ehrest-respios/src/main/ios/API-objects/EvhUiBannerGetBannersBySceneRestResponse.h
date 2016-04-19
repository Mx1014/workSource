//
// EvhUiBannerGetBannersBySceneRestResponse.h
// generated at 2016-04-19 13:40:02 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiBannerGetBannersBySceneRestResponse
//
@interface EvhUiBannerGetBannersBySceneRestResponse : EvhRestResponseBase

// array of EvhBannerDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
