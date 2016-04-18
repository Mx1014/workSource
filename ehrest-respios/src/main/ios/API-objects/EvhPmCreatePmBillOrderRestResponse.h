//
// EvhPmCreatePmBillOrderRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhOrganizationOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmCreatePmBillOrderRestResponse
//
@interface EvhPmCreatePmBillOrderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOrganizationOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
