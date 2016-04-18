//
// EvhAdminTechparkParkSearchApplyCardListRestResponse.h
// generated at 2016-04-18 14:48:52 
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
