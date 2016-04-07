//
// EvhPmFindPmBillByOrderNoRestResponse.h
// generated at 2016-04-07 17:33:50 
//
#import "RestResponseBase.h"
#import "EvhPmBillForOrderNoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmFindPmBillByOrderNoRestResponse
//
@interface EvhPmFindPmBillByOrderNoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmBillForOrderNoDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
