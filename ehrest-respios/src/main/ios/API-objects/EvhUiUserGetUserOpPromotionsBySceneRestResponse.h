//
// EvhUiUserGetUserOpPromotionsBySceneRestResponse.h
// generated at 2016-03-31 20:15:34 
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
