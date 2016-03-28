//
// EvhPmFindPmBillByOrderNoRestResponse.h
// generated at 2016-03-25 19:05:21 
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
