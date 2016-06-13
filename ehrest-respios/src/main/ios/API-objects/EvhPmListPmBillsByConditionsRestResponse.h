//
// EvhPmListPmBillsByConditionsRestResponse.h
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
