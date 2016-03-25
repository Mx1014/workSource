//
// EvhBusinessGetBusinessesByCategoryRestResponse.h
// generated at 2016-03-25 11:43:35 
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
