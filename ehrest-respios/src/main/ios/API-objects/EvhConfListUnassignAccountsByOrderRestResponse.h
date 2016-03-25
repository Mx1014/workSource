//
// EvhConfListUnassignAccountsByOrderRestResponse.h
// generated at 2016-03-25 11:43:35 
//
#import "RestResponseBase.h"
#import "EvhUnassignAccountResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListUnassignAccountsByOrderRestResponse
//
@interface EvhConfListUnassignAccountsByOrderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUnassignAccountResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
