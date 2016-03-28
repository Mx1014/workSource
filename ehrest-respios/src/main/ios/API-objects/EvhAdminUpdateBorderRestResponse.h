//
// EvhAdminUpdateBorderRestResponse.h
// generated at 2016-03-25 19:05:21 
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
