//
// EvhUiUserGetUserOpPromotionsBySceneRestResponse.h
// generated at 2016-04-07 10:47:33 
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
