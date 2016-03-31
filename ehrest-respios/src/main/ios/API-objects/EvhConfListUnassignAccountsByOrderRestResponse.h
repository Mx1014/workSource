//
// EvhConfListUnassignAccountsByOrderRestResponse.h
// generated at 2016-03-28 15:56:09 
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
