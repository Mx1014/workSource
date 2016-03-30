//
// EvhPmListPmBillsByConditionsRestResponse.h
// generated at 2016-03-30 10:13:09 
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
