//
// EvhPromotionTest2RestResponse.h
//
#import "RestResponseBase.h"
#import "EvhOpPromotionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPromotionTest2RestResponse
//
@interface EvhPromotionTest2RestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOpPromotionDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
