//
// EvhAdminPromotionClosePromotionRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetOpPromotionActivityByPromotionId.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPromotionClosePromotionRestResponse
//
@interface EvhAdminPromotionClosePromotionRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetOpPromotionActivityByPromotionId* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
