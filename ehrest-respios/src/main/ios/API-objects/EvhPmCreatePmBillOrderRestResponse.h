//
// EvhPmCreatePmBillOrderRestResponse.h
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
