//
// EvhAdminPromotionGetPromotionByIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetOpPromotionActivityByPromotionId.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPromotionGetPromotionByIdRestResponse
//
@interface EvhAdminPromotionGetPromotionByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetOpPromotionActivityByPromotionId* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
