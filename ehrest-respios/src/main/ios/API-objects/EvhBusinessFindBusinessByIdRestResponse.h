//
// EvhBusinessFindBusinessByIdRestResponse.h
// generated at 2016-03-25 11:43:35 
//
#import "RestResponseBase.h"
#import "EvhBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessFindBusinessByIdRestResponse
//
@interface EvhBusinessFindBusinessByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBusinessDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
