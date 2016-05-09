//
// EvhUserListPostedTopicsRestResponse.h
// generated at 2016-04-29 18:56:04 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListPostedTopicsRestResponse
//
@interface EvhUserListPostedTopicsRestResponse : EvhRestResponseBase

// array of EvhPostDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
