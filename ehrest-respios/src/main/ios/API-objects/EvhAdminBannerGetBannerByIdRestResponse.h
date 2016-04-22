//
// EvhAdminBannerGetBannerByIdRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhBannerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminBannerGetBannerByIdRestResponse
//
@interface EvhAdminBannerGetBannerByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBannerDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
