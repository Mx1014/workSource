//
// EvhPmListPmBillsByConditionsRestResponse.h
// generated at 2016-03-28 15:56:09 
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
