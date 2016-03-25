//
// EvhUiBannerGetBannersBySceneRestResponse.h
// generated at 2016-03-25 11:43:35 
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
