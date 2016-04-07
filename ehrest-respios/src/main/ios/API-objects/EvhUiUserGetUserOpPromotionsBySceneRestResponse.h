//
// EvhUiUserGetUserOpPromotionsBySceneRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhListUserOpPromotionsRespose.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserGetUserOpPromotionsBySceneRestResponse
//
@interface EvhUiUserGetUserOpPromotionsBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListUserOpPromotionsRespose* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
