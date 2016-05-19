//
// EvhAdminPromotionSearchPromotionRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListOpPromotionActivityResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPromotionSearchPromotionRestResponse
//
@interface EvhAdminPromotionSearchPromotionRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOpPromotionActivityResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
