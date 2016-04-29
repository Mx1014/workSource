//
// EvhListTopicsByTypeCommandResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

