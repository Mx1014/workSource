//
// EvhListGroupByTagCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupByTagCommand
//
@interface EvhListGroupByTagCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* searchVisibilityScope;

@property(nonatomic, copy) NSNumber* searchVisibilityScopeId;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

