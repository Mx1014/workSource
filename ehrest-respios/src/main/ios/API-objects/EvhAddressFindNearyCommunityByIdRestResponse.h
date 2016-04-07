//
// EvhAddressFindNearyCommunityByIdRestResponse.h
// generated at 2016-04-07 15:16:53 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressFindNearyCommunityByIdRestResponse
//
@interface EvhAddressFindNearyCommunityByIdRestResponse : EvhRestResponseBase

// array of EvhCommunityDoc* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
