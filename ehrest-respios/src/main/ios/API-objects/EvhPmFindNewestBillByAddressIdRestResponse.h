//
// EvhPmFindNewestBillByAddressIdRestResponse.h
// generated at 2016-04-19 14:25:58 
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
