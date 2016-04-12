//
// EvhSearchTopicsByTypeResponse.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationTaskDTO2.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTopicsByTypeResponse
//
@interface EvhSearchTopicsByTypeResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhOrganizationTaskDTO2*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSString* keywords;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

