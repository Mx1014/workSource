//
// EvhAclinkGetDoorAccessCapapilityRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAccessCapapilityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkGetDoorAccessCapapilityRestResponse
//
@interface EvhAclinkGetDoorAccessCapapilityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAccessCapapilityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
