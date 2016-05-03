//
// EvhPmsyListPmBillsRestResponse.h
// generated at 2016-04-29 18:56:04 
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
