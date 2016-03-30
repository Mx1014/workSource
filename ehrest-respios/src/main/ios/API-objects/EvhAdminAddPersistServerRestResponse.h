//
// EvhAdminAddPersistServerRestResponse.h
// generated at 2016-03-30 10:13:09 
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
