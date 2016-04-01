//
// EvhFeedbackCommand.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFeedbackCommand
//
@interface EvhFeedbackCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* feedbackType;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* proofResourceUri;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

