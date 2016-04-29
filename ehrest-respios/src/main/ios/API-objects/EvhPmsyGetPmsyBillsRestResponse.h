//
// EvhPmsyGetPmsyBillsRestResponse.h
// generated at 2016-04-29 18:56:04 
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
