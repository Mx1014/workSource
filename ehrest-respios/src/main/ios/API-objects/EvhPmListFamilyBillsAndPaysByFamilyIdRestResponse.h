//
// EvhPmListFamilyBillsAndPaysByFamilyIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListFamilyBillsAndPaysByFamilyIdCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListFamilyBillsAndPaysByFamilyIdRestResponse
//
@interface EvhPmListFamilyBillsAndPaysByFamilyIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListFamilyBillsAndPaysByFamilyIdCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
