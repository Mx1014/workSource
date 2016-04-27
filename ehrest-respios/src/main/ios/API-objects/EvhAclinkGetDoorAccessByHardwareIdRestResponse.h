//
// EvhAclinkGetDoorAccessByHardwareIdRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhListDoorAccessResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkGetDoorAccessByHardwareIdRestResponse
//
@interface EvhAclinkGetDoorAccessByHardwareIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAccessResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
