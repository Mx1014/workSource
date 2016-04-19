//
// EvhAdminUpdatePersistServerRestResponse.h
// generated at 2016-04-19 13:40:01 
//
#import "RestResponseBase.h"
#import "EvhServerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUpdatePersistServerRestResponse
//
@interface EvhAdminUpdatePersistServerRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhServerDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
