//
// EvhPmFindPmBillByOrderNoRestResponse.h
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
