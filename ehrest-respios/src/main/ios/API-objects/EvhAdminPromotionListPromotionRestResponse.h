//
// EvhAdminPromotionListPromotionRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListOpPromotionActivityResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPromotionListPromotionRestResponse
//
@interface EvhAdminPromotionListPromotionRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOpPromotionActivityResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
