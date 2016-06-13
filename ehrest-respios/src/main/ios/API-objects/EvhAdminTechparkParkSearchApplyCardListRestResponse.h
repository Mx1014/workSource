//
// EvhAdminTechparkParkSearchApplyCardListRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhApplyParkCardList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminTechparkParkSearchApplyCardListRestResponse
//
@interface EvhAdminTechparkParkSearchApplyCardListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhApplyParkCardList* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
