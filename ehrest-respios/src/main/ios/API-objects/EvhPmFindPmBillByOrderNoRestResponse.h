//
// EvhPmFindPmBillByOrderNoRestResponse.h
// generated at 2016-04-06 19:59:47 
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
