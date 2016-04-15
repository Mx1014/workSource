//
// EvhConfListUnassignAccountsByOrderRestResponse.h
// generated at 2016-04-12 15:02:21 
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
