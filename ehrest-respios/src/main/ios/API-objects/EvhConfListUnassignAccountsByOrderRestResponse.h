//
// EvhConfListUnassignAccountsByOrderRestResponse.h
// generated at 2016-03-31 13:49:15 
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
