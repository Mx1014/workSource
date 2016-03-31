//
// EvhAdminBannerListBannersRestResponse.h
// generated at 2016-03-31 13:49:15 
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
