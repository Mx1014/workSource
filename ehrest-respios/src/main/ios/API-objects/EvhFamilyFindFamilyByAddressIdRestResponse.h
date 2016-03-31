//
// EvhFamilyFindFamilyByAddressIdRestResponse.h
// generated at 2016-03-31 10:18:21 
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
