//
// EvhAdminAddBorderRestResponse.h
// generated at 2016-04-01 15:40:24 
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
