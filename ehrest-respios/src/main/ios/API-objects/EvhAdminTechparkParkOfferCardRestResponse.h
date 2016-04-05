//
// EvhAdminTechparkParkOfferCardRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhApplyParkCardList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminTechparkParkOfferCardRestResponse
//
@interface EvhAdminTechparkParkOfferCardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhApplyParkCardList* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
