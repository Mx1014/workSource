//
// EvhListTopicsByTypeCommandResponse.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationTaskDTO2.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListTopicsByTypeCommandResponse
//
@interface EvhListTopicsByTypeCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhOrganizationTaskDTO2*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

