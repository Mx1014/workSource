//
// EvhPmsyListPmBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhPmsyBillsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyListPmBillsRestResponse
//
@interface EvhPmsyListPmBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmsyBillsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
