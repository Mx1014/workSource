//
// EvhRentalAdminGetResourceTypeListRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetResourceTypeListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminGetResourceTypeListRestResponse
//
@interface EvhRentalAdminGetResourceTypeListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetResourceTypeListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
