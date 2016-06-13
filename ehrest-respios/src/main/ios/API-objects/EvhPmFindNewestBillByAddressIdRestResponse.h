//
// EvhPmFindNewestBillByAddressIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhPmBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmFindNewestBillByAddressIdRestResponse
//
@interface EvhPmFindNewestBillByAddressIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmBillsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
