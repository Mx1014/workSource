//
// EvhAdminBannerListBannersRestResponse.h
// generated at 2016-04-07 10:47:32 
//
#import "RestResponseBase.h"
#import "EvhListBannersAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminBannerListBannersRestResponse
//
@interface EvhAdminBannerListBannersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListBannersAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
