//
// EvhPmCreatePmBillOrderRestResponse.h
// generated at 2016-03-31 13:49:15 
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
