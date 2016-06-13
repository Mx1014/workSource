//
// EvhAdminPromotionCreatePromotionRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhOpPromotionActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminPromotionCreatePromotionRestResponse
//
@interface EvhAdminPromotionCreatePromotionRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOpPromotionActivityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
