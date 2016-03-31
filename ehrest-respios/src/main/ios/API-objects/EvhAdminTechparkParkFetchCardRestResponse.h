//
// EvhAdminTechparkParkFetchCardRestResponse.h
// generated at 2016-03-31 19:08:54 
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
