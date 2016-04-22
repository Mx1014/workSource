//
// EvhListGroupByTagCommand.h
// generated at 2016-04-22 13:56:44 
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

