//
// EvhPmFindFamilyByAddressIdRestResponse.h
// generated at 2016-04-26 18:22:57 
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
