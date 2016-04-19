//
// EvhPmFindPmBillByOrderNoRestResponse.h
// generated at 2016-04-19 13:40:02 
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
