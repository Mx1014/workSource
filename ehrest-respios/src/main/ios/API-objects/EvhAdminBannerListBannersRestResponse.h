//
// EvhAdminBannerListBannersRestResponse.h
// generated at 2016-04-18 14:48:52 
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
