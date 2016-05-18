//
// EvhBusinessGetBusinessesByCategoryRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetBusinessesByCategoryCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessGetBusinessesByCategoryRestResponse
//
@interface EvhBusinessGetBusinessesByCategoryRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetBusinessesByCategoryCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
