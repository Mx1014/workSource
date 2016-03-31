//
// EvhBannerGetBannersRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerGetBannersRestResponse
//
@interface EvhBannerGetBannersRestResponse : EvhRestResponseBase

// array of EvhBannerDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
