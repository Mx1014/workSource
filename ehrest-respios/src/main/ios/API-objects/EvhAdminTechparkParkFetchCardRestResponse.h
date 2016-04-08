//
// EvhAdminTechparkParkFetchCardRestResponse.h
// generated at 2016-04-08 20:09:23 
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
