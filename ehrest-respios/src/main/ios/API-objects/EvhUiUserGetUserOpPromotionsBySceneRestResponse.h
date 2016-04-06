//
// EvhUiUserGetUserOpPromotionsBySceneRestResponse.h
// generated at 2016-04-06 19:10:44 
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
