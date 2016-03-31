//
// EvhPmFindBillByAddressIdAndTimeRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhPmBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmFindBillByAddressIdAndTimeRestResponse
//
@interface EvhPmFindBillByAddressIdAndTimeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmBillsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
