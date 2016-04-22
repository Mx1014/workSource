//
// EvhAdminAddPersistServerRestResponse.h
// generated at 2016-04-22 13:56:49 
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
