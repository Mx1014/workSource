//
// EvhAddressClaimAddressV2RestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressClaimAddressV2RestResponse
//
@interface EvhAddressClaimAddressV2RestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFamilyDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
