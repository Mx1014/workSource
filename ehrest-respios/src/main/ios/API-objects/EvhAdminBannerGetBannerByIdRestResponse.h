//
// EvhAdminBannerGetBannerByIdRestResponse.h
// generated at 2016-03-31 20:15:33 
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
