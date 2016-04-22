//
// EvhPmFindBillByAddressIdAndTimeRestResponse.h
// generated at 2016-04-22 13:56:50 
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
