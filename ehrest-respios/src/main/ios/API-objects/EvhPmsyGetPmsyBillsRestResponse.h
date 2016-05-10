//
// EvhPmsyGetPmsyBillsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhPmsyBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyGetPmsyBillsRestResponse
//
@interface EvhPmsyGetPmsyBillsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmsyBillsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
