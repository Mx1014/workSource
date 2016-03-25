//
// EvhPmCreatePmBillOrderRestResponse.h
// generated at 2016-03-25 09:26:44 
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
