//
// EvhAdminUpdateBorderRestResponse.h
// generated at 2016-04-22 13:56:50 
//
#import "RestResponseBase.h"
#import "EvhBorderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUpdateBorderRestResponse
//
@interface EvhAdminUpdateBorderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBorderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
