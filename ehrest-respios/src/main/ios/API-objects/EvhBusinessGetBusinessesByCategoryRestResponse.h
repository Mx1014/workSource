//
// EvhBusinessGetBusinessesByCategoryRestResponse.h
// generated at 2016-04-26 18:22:56 
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
