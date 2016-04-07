//
// EvhBusinessGetBusinessesByCategoryRestResponse.h
// generated at 2016-04-07 17:03:18 
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
