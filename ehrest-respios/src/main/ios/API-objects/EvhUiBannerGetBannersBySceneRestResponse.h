//
// EvhUiBannerGetBannersBySceneRestResponse.h
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
