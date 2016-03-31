//
// EvhAclinkGetDoorAccessByHardwareIdRestResponse.h
// generated at 2016-03-31 15:43:23 
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
