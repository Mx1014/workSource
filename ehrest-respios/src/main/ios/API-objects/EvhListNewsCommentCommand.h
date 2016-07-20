//
// EvhListNewsCommentCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNewsCommentCommand
//
@interface EvhListNewsCommentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* theNewsToken;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

