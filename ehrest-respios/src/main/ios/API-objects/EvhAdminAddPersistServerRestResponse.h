//
// EvhAdminAddPersistServerRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhServerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAddPersistServerRestResponse
//
@interface EvhAdminAddPersistServerRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhServerDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
