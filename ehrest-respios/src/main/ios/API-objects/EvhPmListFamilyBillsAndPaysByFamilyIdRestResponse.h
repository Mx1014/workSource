//
// EvhPmListFamilyBillsAndPaysByFamilyIdRestResponse.h
// generated at 2016-03-31 13:49:15 
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
