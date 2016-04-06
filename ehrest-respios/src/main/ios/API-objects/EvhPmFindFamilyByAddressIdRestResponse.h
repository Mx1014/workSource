//
// EvhPmFindFamilyByAddressIdRestResponse.h
// generated at 2016-04-06 19:10:44 
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
