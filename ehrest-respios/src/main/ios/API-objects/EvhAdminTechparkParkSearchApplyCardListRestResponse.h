//
// EvhAdminTechparkParkSearchApplyCardListRestResponse.h
// generated at 2016-04-08 20:09:23 
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
