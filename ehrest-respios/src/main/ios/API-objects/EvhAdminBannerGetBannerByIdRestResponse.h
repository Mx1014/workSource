//
// EvhAdminBannerGetBannerByIdRestResponse.h
// generated at 2016-03-25 17:08:12 
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
