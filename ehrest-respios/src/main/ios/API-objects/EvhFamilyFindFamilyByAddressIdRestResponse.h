//
// EvhFamilyFindFamilyByAddressIdRestResponse.h
// generated at 2016-04-07 17:03:18 
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
