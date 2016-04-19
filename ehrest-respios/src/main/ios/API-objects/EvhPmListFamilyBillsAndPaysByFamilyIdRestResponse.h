//
// EvhPmListFamilyBillsAndPaysByFamilyIdRestResponse.h
// generated at 2016-04-19 14:25:58 
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
