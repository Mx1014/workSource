//
// EvhFeedbackCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
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

