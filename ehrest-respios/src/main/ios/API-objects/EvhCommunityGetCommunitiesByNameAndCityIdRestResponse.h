//
// EvhCommunityGetCommunitiesByNameAndCityIdRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityGetCommunitiesByNameAndCityIdRestResponse
//
@interface EvhCommunityGetCommunitiesByNameAndCityIdRestResponse : EvhRestResponseBase

// array of EvhCommunityDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
