//
// EvhUiForumGetTopicQueryFiltersRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiForumGetTopicQueryFiltersRestResponse
//
@interface EvhUiForumGetTopicQueryFiltersRestResponse : EvhRestResponseBase

// array of EvhTopicFilterDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
