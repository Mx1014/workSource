//
// EvhUiForumGetTopicQueryFiltersRestResponse.h
// generated at 2016-03-30 10:13:09 
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
