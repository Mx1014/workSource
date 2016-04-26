//
// EvhAdminTechparkParkFetchCardRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhApplyParkCardDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminTechparkParkFetchCardRestResponse
//
@interface EvhAdminTechparkParkFetchCardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhApplyParkCardDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
