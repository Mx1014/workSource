//
// EvhAdminUpdatePersistServerRestResponse.h
// generated at 2016-04-18 14:48:52 
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
