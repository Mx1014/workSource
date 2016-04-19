//
// EvhPmListPmBillsByConditionsRestResponse.h
// generated at 2016-04-19 14:25:58 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPmBillsByConditionsRestResponse
//
@interface EvhPmListPmBillsByConditionsRestResponse : EvhRestResponseBase

// array of EvhPmBillsDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
