//
// EvhFamilyGetRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyGetRestResponse
//
@interface EvhFamilyGetRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhFamilyDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
