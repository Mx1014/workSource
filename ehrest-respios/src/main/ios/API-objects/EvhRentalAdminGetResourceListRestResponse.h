//
// EvhRentalAdminGetResourceListRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetResourceListAdminResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetResourceListRestResponse
//
@interface EvhRentalAdminGetResourceListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetResourceListAdminResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
