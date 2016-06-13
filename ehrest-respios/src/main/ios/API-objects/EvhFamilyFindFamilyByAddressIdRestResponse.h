//
// EvhFamilyFindFamilyByAddressIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyFindFamilyByAddressIdRestResponse
//
@interface EvhFamilyFindFamilyByAddressIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFamilyDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
