//
// EvhBannerGetBannerByIdCommandRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhBannerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerGetBannerByIdCommandRestResponse
//
@interface EvhBannerGetBannerByIdCommandRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBannerDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
