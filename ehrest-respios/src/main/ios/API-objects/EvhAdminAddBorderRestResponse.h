//
// EvhAdminAddBorderRestResponse.h
// generated at 2016-03-25 17:08:12 
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
