//
// EvhAdminAddBorderRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhBorderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAddBorderRestResponse
//
@interface EvhAdminAddBorderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBorderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
