//
// EvhAclinkGetDoorAccessCapapilityCommandRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAccessCapapilityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkGetDoorAccessCapapilityCommandRestResponse
//
@interface EvhAclinkGetDoorAccessCapapilityCommandRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAccessCapapilityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
