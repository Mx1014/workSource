//
// EvhBannerGetBannerByIdCommandRestResponse.h
// generated at 2016-03-31 13:49:15 
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
