//
// EvhAdminTechparkParkSearchApplyCardListRestResponse.h
// generated at 2016-04-12 19:00:53 
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
