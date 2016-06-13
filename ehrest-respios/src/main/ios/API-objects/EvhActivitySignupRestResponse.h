//
// EvhActivitySignupRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivitySignupRestResponse
//
@interface EvhActivitySignupRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhActivityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
