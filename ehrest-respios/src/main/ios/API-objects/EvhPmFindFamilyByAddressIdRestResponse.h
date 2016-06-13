//
// EvhPmFindFamilyByAddressIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhPropFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmFindFamilyByAddressIdRestResponse
//
@interface EvhPmFindFamilyByAddressIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPropFamilyDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
