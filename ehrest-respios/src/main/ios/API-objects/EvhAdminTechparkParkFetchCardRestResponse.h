//
// EvhAdminTechparkParkFetchCardRestResponse.h
// generated at 2016-04-18 14:48:52 
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
