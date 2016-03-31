//
// EvhPmFindFamilyByAddressIdRestResponse.h
// generated at 2016-03-28 15:56:09 
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
